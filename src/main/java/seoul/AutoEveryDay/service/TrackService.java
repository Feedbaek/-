package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.TrackDto;
import seoul.AutoEveryDay.dto.DriveHistoryDto;
import seoul.AutoEveryDay.entity.Track;
import seoul.AutoEveryDay.entity.DriveHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.TrackRepository;
import seoul.AutoEveryDay.repository.DriveHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j(topic = "TrackService")
@Transactional
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final DriveHistoryRepository driveHistoryRepository;

    private void validateTestHistory(DriveHistoryDto testHistory, User user) {
        if (testHistory.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약은 오늘 이후로만 가능합니다.");
        }
        if (driveHistoryRepository.findByTrackIdAndDate(user.getId(), testHistory.getDate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약한 날짜입니다.");
        }
    }
    public Track getTestTrack(String name) {
        return trackRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "테스트 센터를 찾을 수 없습니다.")
        );
    }
    public Track getTestTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "테스트 센터를 찾을 수 없습니다.")
        );
    }

    public TrackDto createTestTrack(TrackDto trackDto) {
        if (trackRepository.existsByName(trackDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 테스트 센터 이름입니다.");
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
        return trackDto;
    }
    public TrackDto editTestTrack(TrackDto trackDto) {
        Track track = getTestTrack(trackDto.getName());
        trackRepository.findByName(trackDto.getName()).ifPresent(
                (t) -> {
                    if (!t.getId().equals(track.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 테스트 센터 이름입니다.");
                    }
                }
        );
        track.setName(trackDto.getName());
        track.setDescription(trackDto.getDescription());
        return trackDto;
    }
    public TrackDto deleteTestTrack(String name) {
        Track track = getTestTrack(name);
        try {
            trackRepository.delete(track);
        } catch (Exception e) {
            log.error("테스트 센터 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 삭제 실패");
        }
        return TrackDto.builder()
                .name(track.getName())
                .description(track.getDescription())
                .build();
    }
    public List<TrackDto> getAllTestTrack() {
        return trackRepository.findAll().stream()
                .map((t) -> TrackDto.builder()
                        .name(t.getName())
                        .description(t.getDescription())
                        .build()
                ).toList();
    }

    // 여기부터 예약 관련
    public DriveHistoryDto createTestHistory(DriveHistoryDto driveHistoryDto, User user) {
        validateTestHistory(driveHistoryDto, user);
        Track track = getTestTrack(driveHistoryDto.getTrackId());
        DriveHistory driveHistory = DriveHistory.builder()
                .user(user)
                .track(track)
                .date(driveHistoryDto.getDate())
                .build();
        try {
            driveHistoryRepository.save(driveHistory);
        } catch (Exception e) {
            log.error("예약 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 저장 실패");
        }
        return driveHistoryDto;
    }

    public DriveHistoryDto deleteTestHistory(DriveHistoryDto driveHistoryDto, User user) {
        Track track = getTestTrack(driveHistoryDto.getTrackId());
        DriveHistory driveHistory = driveHistoryRepository.findByUserIdAndTrackIdAndDate(user.getId(), track.getId(), driveHistoryDto.getDate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        try {
            driveHistoryRepository.delete(driveHistory);
        } catch (Exception e) {
            log.error("예약 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 삭제 실패");
        }
        return driveHistoryDto;
    }

}
