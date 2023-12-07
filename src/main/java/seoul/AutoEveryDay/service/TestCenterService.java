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

import java.util.List;

@Service
@Slf4j(topic = "TestCenterService")
@Transactional
@RequiredArgsConstructor
public class TestCenterService {
    private final TestCenterRepository testCenterRepository;
    private final TestHistoryRepository testHistoryRepository;

    private TestCenter save(TestCenter testCenter) {
        try {
            return testCenterRepository.save(testCenter);
        } catch (Exception e) {
            log.error("테스트 센터 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 센터 저장 실패");
        }
    }
    private void validateTestHistory(TestHistory testHistory) {
        if (testHistoryRepository.findByTestCenterIdAndTime(testHistory.getTestCenter().getId(), testHistory.getTime()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 시간입니다.");
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
        save(testCenter);
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
        testCenterRepository.delete(testCenter);
        return TestCenterDto.builder()
                .name(testCenter.getName())
                .address(testCenter.getAddress())
                .build();
    }
    public List<TestCenter> getAllTestCenter() {
        return testCenterRepository.findAll();
    }

    // 여기부터 예약 관련
    public TestHistory save(TestHistory testHistory) {
        try {
            return testHistoryRepository.save(testHistory);
        } catch (Exception e) {
            log.error("예약 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 저장 실패");
        }
    }

    public TestHistory createTestHistory(TestHistoryDto testHistoryDto, User user) {
        TestHistory testHistory = TestHistory.builder()
                .user(user)
                .testCenter(getTestCenter(testHistoryDto.getTestCenterName()))
                .time(testHistoryDto.getTime())
                .build();
        validateTestHistory(testHistory);
        save(testHistory);
        return testHistory;
    }

    public TestHistory deleteTestHistory(TestHistoryDto testHistoryDto, User user) {
        TestCenter testCenter = getTestCenter(testHistoryDto.getTestCenterName());
        TestHistory testHistory = testHistoryRepository.findByUserIdAndTestCenterIdAndTime(user.getId(), testCenter.getId(), testHistoryDto.getTime()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.")
        );
        testHistoryRepository.delete(testHistory);
        return testHistory;
    }

}
