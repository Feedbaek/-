package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.TestCenterDto;
import seoul.AutoEveryDay.dto.TestHistoryDto;
import seoul.AutoEveryDay.entity.TestCenter;
import seoul.AutoEveryDay.entity.TestHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.TestCenterRepository;
import seoul.AutoEveryDay.repository.TestHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j(topic = "TestCenterService")
@Transactional
@RequiredArgsConstructor
public class TestCenterService {
    private final TestCenterRepository testCenterRepository;
    private final TestHistoryRepository testHistoryRepository;

    private void validateTestHistory(TestHistoryDto testHistory, User user) {
        if (testHistory.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약은 오늘 이후로만 가능합니다.");
        }
        if (testHistoryRepository.findByTestCenterIdAndDate(user.getId(), testHistory.getDate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약한 날짜입니다.");
        }
    }

    public TestCenterDto createTestCenter(TestCenterDto testCenterDto) {
        if (testCenterRepository.existsByName(testCenterDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 테스트 센터 이름입니다.");
        }
        TestCenter testCenter = TestCenter.builder()
                .name(testCenterDto.getName())
                .address(testCenterDto.getAddress())
                .build();
        try {
            testCenterRepository.save(testCenter);
        } catch (Exception e) {
            log.error("테스트 센터 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 저장 실패");
        }
        return testCenterDto;
    }
    public TestCenter getTestCenter(String name) {
        return testCenterRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "테스트 센터를 찾을 수 없습니다.")
        );
    }
    public TestCenterDto editTestCenter(TestCenterDto testCenterDto) {
        TestCenter testCenter = getTestCenter(testCenterDto.getName());
        testCenterRepository.findByName(testCenterDto.getName()).ifPresent(
                (t) -> {
                    if (!t.getId().equals(testCenter.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 테스트 센터 이름입니다.");
                    }
                }
        );
        testCenter.setName(testCenterDto.getName());
        testCenter.setAddress(testCenterDto.getAddress());
        return testCenterDto;
    }
    public TestCenterDto deleteTestCenter(String name) {
        TestCenter testCenter = getTestCenter(name);
        try {
            testCenterRepository.delete(testCenter);
        } catch (Exception e) {
            log.error("테스트 센터 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 삭제 실패");
        }
        return TestCenterDto.builder()
                .name(testCenter.getName())
                .address(testCenter.getAddress())
                .build();
    }
    public List<TestCenter> getAllTestCenter() {
        return testCenterRepository.findAll();
    }

    // 여기부터 예약 관련
    public TestHistoryDto createTestHistory(TestHistoryDto testHistoryDto, User user) {
        validateTestHistory(testHistoryDto, user);
        TestHistory testHistory = TestHistory.builder()
                .user(user)
                .testCenter(getTestCenter(testHistoryDto.getTestCenterName()))
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
        TestCenter testCenter = getTestCenter(testHistoryDto.getTestCenterName());
        TestHistory testHistory = testHistoryRepository.findByUserIdAndTestCenterIdAndDate(user.getId(), testCenter.getId(), testHistoryDto.getDate()).orElseThrow(
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
