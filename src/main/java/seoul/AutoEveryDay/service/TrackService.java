package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.track.TrackDto;
import seoul.AutoEveryDay.dto.track.ReserveTrackDto;
import seoul.AutoEveryDay.dto.track.TrackReq;
import seoul.AutoEveryDay.entity.Track;
import seoul.AutoEveryDay.entity.ReserveTrack;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.TrackRepository;
import seoul.AutoEveryDay.repository.ReserveTrackRepository;
import seoul.AutoEveryDay.utils.Converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "TrackService")
@Transactional
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final ReserveTrackRepository reserveTrackRepository;
    private final Converter converter;

    private void validateReserveHistory(ReserveTrackDto testHistory) {
        if (testHistory.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약은 오늘 이후로만 가능합니다.");
        }
        if (reserveTrackRepository.findByTrack_IdAndDateAndIsCanceled(testHistory.getTrackId(), testHistory.getDate(), false).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 날짜입니다.");
        }
    }
    private Track getTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "트랙을 찾을 수 없습니다.")
        );
    }

    public TrackDto createTestTrack(TrackDto trackDto) { // 테스트 코드용
        if (trackRepository.existsByName(trackDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 트랙 입니다.");
        }
        Track track = Track.builder()
                .name(trackDto.getName())
                .description(trackDto.getDescription())
                .build();
        try {
            trackRepository.save(track);
        } catch (Exception e) {
            log.error("테스트 센터 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 저장 실패");
        }
        trackDto.setId(track.getId());
        return trackDto;
    }

    public TrackDto createTestTrack(TrackReq trackReq) {
        if (trackRepository.existsByName(trackReq.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 트랙 입니다.");
        }
        if (!converter.isActualJPEG(trackReq.getImage())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다.");
        }
        Track track = Track.builder()
                .name(trackReq.getName())
                .description(trackReq.getDescription())
                .build();
        try {
            trackRepository.save(track);
            String fileName = track.getId() + ".jpeg";
            String imageUrl = converter.convertImgToUrl(trackReq.getImage(), "/track", fileName);
            track.setImage(imageUrl);
        } catch (Exception e) {
            log.error("테스트 센터 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 저장 실패");
        }
        return TrackDto.builder()
                .id(track.getId())
                .name(track.getName())
                .description(track.getDescription())
                .image(track.getImage())
                .build();
    }

    public TrackDto editTestTrack(TrackDto trackDto) {  // 테스트 코드용
        Track track = getTrack(trackDto.getId());
        trackRepository.findByName(trackDto.getName()).ifPresent(
                (t) -> {
                    if (!t.getId().equals(track.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 트랙 입니다.");
                    }
                }
        );
        track.setName(trackDto.getName());
        track.setDescription(trackDto.getDescription());
        return trackDto;
    }

    public TrackDto editTestTrack(TrackReq trackReq) {
        Track track = getTrack(trackReq.getId());
        if (!converter.isActualJPEG(trackReq.getImage())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다.");
        }

        trackRepository.findByName(trackReq.getName()).ifPresent(
                (t) -> {
                    if (!t.getId().equals(track.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 트랙 입니다.");
                    }
                }
        );
        String imageUrl = converter.convertImgToUrl(trackReq.getImage(), "/track", track.getId() + ".jpeg");

        track.setName(trackReq.getName());
        track.setDescription(trackReq.getDescription());
        track.setImage(imageUrl);

        return TrackDto.builder()
                .id(track.getId())
                .name(track.getName())
                .description(track.getDescription())
                .image(track.getImage())
                .build();
    }

    public TrackDto deleteTestTrack(Long id) {
        Track track = getTrack(id);
        if (reserveTrackRepository.existsByTrack_IdAndDateGreaterThanEqualAndIsCanceled(track.getId(), LocalDate.now(), false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약된 트랙은 삭제할 수 없습니다.");
        }
        try {
            trackRepository.delete(track);
        } catch (Exception e) {
            log.error("테스트 센터 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 삭제 실패");
        }
        return TrackDto.builder()
                .id(track.getId())
                .name(track.getName())
                .description(track.getDescription())
                .build();
    }
    public List<TrackDto> getAllTestTrack() {
        return trackRepository.findAll().stream()
                .map((t) -> TrackDto.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .description(t.getDescription())
                        .image(t.getImage())
                        .build()
                ).toList();
    }

    // 여기부터 예약 관련
    public ReserveTrackDto createReserveHistory(ReserveTrackDto reserveTrackDto, User user) {
        validateReserveHistory(reserveTrackDto);
        Track track = getTrack(reserveTrackDto.getTrackId());
        ReserveTrack reserveTrack = ReserveTrack.builder()
                .user(user)
                .track(track)
                .date(reserveTrackDto.getDate())
                .isCanceled(false)
                .build();
        try {
            reserveTrackRepository.save(reserveTrack);
        } catch (Exception e) {
            log.error("예약 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 저장 실패");
        }
        reserveTrack.setId(reserveTrack.getId());
        return reserveTrackDto;
    }

    public ReserveTrackDto deleteReserveHistory(ReserveTrackDto reserveTrackDto, User user) {
        Track track = getTrack(reserveTrackDto.getTrackId());
        ReserveTrack reserveTrack = reserveTrackRepository.findByUser_IdAndTrack_IdAndDate(user.getId(), track.getId(), reserveTrackDto.getDate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        reserveTrackDto.setId(reserveTrack.getId());
        try {
            reserveTrackRepository.delete(reserveTrack);
        } catch (Exception e) {
            log.error("예약 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 삭제 실패");
        }
        return reserveTrackDto;
    }

    public ReserveTrackDto deleteReserveHistory(Long historyId) {
        ReserveTrack reserveTrack = reserveTrackRepository.findById(historyId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        ReserveTrackDto reserveTrackDto = ReserveTrackDto.builder()
                .id(reserveTrack.getId())
                .userId(reserveTrack.getUser().getId())
                .trackId(reserveTrack.getTrack().getId())
                .date(reserveTrack.getDate())
                .build();
        try {
            reserveTrackRepository.delete(reserveTrack);
        } catch (Exception e) {
            log.error("예약 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 삭제 실패");
        }
        return reserveTrackDto;
    }

    public Track getTestTrack(Long trackId) {
        return trackRepository.findById(trackId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "트랙을 찾을 수 없습니다.")
        );
    }

    public List<String> getUnavailableDateList(Track track) {
        // 오늘 이후 예약 날짜를 가져옴
        List<ReserveTrack> reserveTrackList = reserveTrackRepository.findByTrack_IdAndDateGreaterThanEqual(track.getId(), LocalDate.now());
        List<String> unavailableDateList = new ArrayList<>();

        reserveTrackList.forEach((reserveTrack) -> {
            String date = reserveTrack.getDate().toString();
            unavailableDateList.add(date);
        });

        return unavailableDateList;
    }

    public List<List<String>> getReserveHistory(User user) {
        List<ReserveTrack> reserveTrackList = reserveTrackRepository.findByUser_Id(user.getId());
        List<List<String>> reserveHistoryList = new ArrayList<>();

        reserveTrackList.forEach((reserveTrack) -> {
            List<String> reserveHistory = new ArrayList<>();

            reserveHistory.add(reserveTrack.getId().toString());
            reserveHistory.add(reserveTrack.getTrack().getName());
            reserveHistory.add(reserveTrack.getDate().toString());
            if (reserveTrack.getIsCanceled()) {
                reserveHistory.add("취소 완료");
            } else if (reserveTrack.getDate().isBefore(LocalDate.now()) || reserveTrack.getDate().isEqual(LocalDate.now())) {
                reserveHistory.add("예약 완료");
            } else {
                reserveHistory.add("예약 신청");
            }
            reserveHistory.add(reserveTrack.getTrack().getImage());

            reserveHistoryList.add(reserveHistory);
        });

        return reserveHistoryList;
    }

    public ReserveTrackDto cancelReserveHistory(Long historyId) {
        ReserveTrack reserveTrack = reserveTrackRepository.findById(historyId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        if (reserveTrack.getIsCanceled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다.");
        }
        reserveTrack.setIsCanceled(true);
        return ReserveTrackDto.builder()
                .id(reserveTrack.getId())
                .userId(reserveTrack.getUser().getId())
                .trackId(reserveTrack.getTrack().getId())
                .date(reserveTrack.getDate())
                .build();
    }
}
