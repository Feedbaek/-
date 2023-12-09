package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.TestTrackDto;
import seoul.AutoEveryDay.dto.TestHistoryDto;
import seoul.AutoEveryDay.entity.TestTrack;
import seoul.AutoEveryDay.entity.TestHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.TestTrackRepository;
import seoul.AutoEveryDay.repository.TestHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j(topic = "TestTrackService")
@Transactional
@RequiredArgsConstructor
public class TestTrackService {
    private final TestTrackRepository testTrackRepository;
    private final TestHistoryRepository testHistoryRepository;

    private void validateTestHistory(TestHistoryDto testHistory, User user) {
        if (testHistory.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약은 오늘 이후로만 가능합니다.");
        }
        if (testHistoryRepository.findByTestTrackIdAndDate(user.getId(), testHistory.getDate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약한 날짜입니다.");
        }
    }
    public TestTrack getTestTrack(String name) {
        return testTrackRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "테스트 센터를 찾을 수 없습니다.")
        );
    }
    public TestTrack getTestTrack(Long id) {
        return testTrackRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "테스트 센터를 찾을 수 없습니다.")
        );
    }

    public TestTrackDto createTestTrack(TestTrackDto testTrackDto) {
        if (testTrackRepository.existsByName(testTrackDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 테스트 센터 이름입니다.");
        }
        TestTrack testTrack = TestTrack.builder()
                .name(testTrackDto.getName())
                .description(testTrackDto.getDescription())
                .build();
        try {
            testTrackRepository.save(testTrack);
        } catch (Exception e) {
            log.error("테스트 센터 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 저장 실패");
        }
        return testTrackDto;
    }
    public TestTrackDto editTestTrack(TestTrackDto testTrackDto) {
        TestTrack testTrack = getTestTrack(testTrackDto.getName());
        testTrackRepository.findByName(testTrackDto.getName()).ifPresent(
                (t) -> {
                    if (!t.getId().equals(testTrack.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 테스트 센터 이름입니다.");
                    }
                }
        );
        testTrack.setName(testTrackDto.getName());
        testTrack.setDescription(testTrackDto.getDescription());
        return testTrackDto;
    }
    public TestTrackDto deleteTestTrack(String name) {
        TestTrack testTrack = getTestTrack(name);
        try {
            testTrackRepository.delete(testTrack);
        } catch (Exception e) {
            log.error("테스트 센터 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 삭제 실패");
        }
        return TestTrackDto.builder()
                .name(testTrack.getName())
                .description(testTrack.getDescription())
                .build();
    }
    public List<TestTrackDto> getAllTestTrack() {
        return testTrackRepository.findAll().stream()
                .map((t) -> TestTrackDto.builder()
                        .name(t.getName())
                        .description(t.getDescription())
                        .build()
                ).toList();
    }

    // 여기부터 예약 관련
    public TestHistoryDto createTestHistory(TestHistoryDto testHistoryDto, User user) {
        validateTestHistory(testHistoryDto, user);
        TestTrack testTrack = getTestTrack(testHistoryDto.getTestTrackId());
        TestHistory testHistory = TestHistory.builder()
                .user(user)
                .testTrack(testTrack)
                .date(testHistoryDto.getDate())
                .build();
        try {
            testHistoryRepository.save(testHistory);
        } catch (Exception e) {
            log.error("예약 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 저장 실패");
        }
        return testHistoryDto;
    }

    public TestHistoryDto deleteTestHistory(TestHistoryDto testHistoryDto, User user) {
        TestTrack testTrack = getTestTrack(testHistoryDto.getTestTrackId());
        TestHistory testHistory = testHistoryRepository.findByUserIdAndTestTrackIdAndDate(user.getId(), testTrack.getId(), testHistoryDto.getDate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        try {
            testHistoryRepository.delete(testHistory);
        } catch (Exception e) {
            log.error("예약 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 삭제 실패");
        }
        return testHistoryDto;
    }

}
