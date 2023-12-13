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

    private void validateDriveHistory(DriveHistoryDto testHistory, User user) {
        if (testHistory.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약은 오늘 이후로만 가능합니다.");
        }
        if (driveHistoryRepository.findByTrackIdAndDate(user.getId(), testHistory.getDate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 날짜입니다.");
        }
    }
    private Track getTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "트랙을 찾을 수 없습니다.")
        );
    }

    public TrackDto createTestTrack(TrackDto trackDto) {
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
    public TrackDto editTestTrack(TrackDto trackDto) {
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
    public TrackDto deleteTestTrack(Long id) {
        Track track = getTrack(id);
        if (driveHistoryRepository.existsByTrackId(track.getId())) {
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
                        .build()
                ).toList();
    }

    // 여기부터 예약 관련
    public DriveHistoryDto createDriveHistory(DriveHistoryDto driveHistoryDto, User user) {
        validateDriveHistory(driveHistoryDto, user);
        Track track = getTrack(driveHistoryDto.getTrackId());
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
        driveHistory.setId(driveHistory.getId());
        return driveHistoryDto;
    }

    public DriveHistoryDto deleteDriveHistory(DriveHistoryDto driveHistoryDto, User user) {
        Track track = getTrack(driveHistoryDto.getTrackId());
        DriveHistory driveHistory = driveHistoryRepository.findByUserIdAndTrackIdAndDate(user.getId(), track.getId(), driveHistoryDto.getDate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        driveHistoryDto.setId(driveHistory.getId());
        try {
            driveHistoryRepository.delete(driveHistory);
        } catch (Exception e) {
            log.error("예약 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 삭제 실패");
        }
        return driveHistoryDto;
    }

}
