package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import seoul.AutoEveryDay.entity.TestCenter;
import seoul.AutoEveryDay.repository.TestCenterRepository;

import java.util.List;

@Service
@Slf4j(topic = "TestCenterService")
@RequiredArgsConstructor
public class TestCenterService {
    private final TestCenterRepository testCenterRepository;

    public String createTestCenter() {
        return "테스트 센터 등록 성공";
    }
    public String getTestCenter(Long id) {
        return "테스트 센터 조회 성공";
    }
    public String editTestCenter() {
        return "테스트 센터 수정 성공";
    }
    public String deleteTestCenter() {
        return "테스트 센터 삭제 성공";
    }
    public List<TestCenter> getAllTestCenter() {
        return null;
    }
}
