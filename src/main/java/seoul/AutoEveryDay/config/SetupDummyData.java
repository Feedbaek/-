package seoul.AutoEveryDay.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.TestCenter;
import seoul.AutoEveryDay.repository.CarRepository;
import seoul.AutoEveryDay.repository.TestCenterRepository;

@Component
@RequiredArgsConstructor
public class SetupDummyData {
    private final CarRepository carRepository;
    private final TestCenterRepository testCenterRepository;

    @PostConstruct
    @Transactional
    public void setupCarDummy() {  // 차량 더미 데이터 생성
        // 아반떼 10대
        for (int i = 0; i < 10; i++) {
            carRepository.save(Car.builder()
                    .number("11가_111" + i)
                    .type("아반떼")
                    .status("정상")
                    .comment("코멘트" + i)
                    .build());
        }
        // 쏘나타 10대
        for (int i = 0; i < 10; i++) {
            carRepository.save(Car.builder()
                    .number("11가_222" + i)
                    .type("쏘나타")
                    .status("정상")
                    .comment("코멘트" + i)
                    .build());
        }
        // 스포티지 10대
        for (int i = 0; i < 10; i++) {
            carRepository.save(Car.builder()
                    .number("22가_111" + i)
                    .type("스포티지")
                    .status("정상")
                    .comment("코멘트" + i)
                    .build());
        }
        // 그랜저 10대
        for (int i = 0; i < 10; i++) {
            carRepository.save(Car.builder()
                    .number("22가_222" + i)
                    .type("그랜저")
                    .status("정상")
                    .comment("코멘트" + i)
                    .build());
        }
        // 제네시스 10대
        for (int i = 0; i < 10; i++) {
            carRepository.save(Car.builder()
                    .number("33가_111" + i)
                    .type("제네시스")
                    .status("정상")
                    .comment("코멘트" + i)
                    .build());
        }
    }

    @PostConstruct
    @Transactional
    public void setupTestCenter() { // 테스트 센터 더미
        testCenterRepository.save(TestCenter.builder()
                .name("강남 시험장")
                .address("서울특별시 강남구 강남대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("강북 시험장")
                .address("서울특별시 강북구 강북대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("강서 시험장")
                .address("서울특별시 강서구 강서대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("강동 시험장")
                .address("서울특별시 강동구 강동대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("서초 시험장")
                .address("서울특별시 서초구 서초대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("성북 시험장")
                .address("서울특별시 성북구 성북대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("성동 시험장")
                .address("서울특별시 성동구 성동대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("중랑 시험장")
                .address("서울특별시 중랑구 중랑대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("노원 시험장")
                .address("서울특별시 노원구 노원대로 123")
                .capacity(30)
                .build());
        testCenterRepository.save(TestCenter.builder()
                .name("도봉 시험장")
                .address("서울특별시 도봉구 도봉대로 123")
                .capacity(30)
                .build());
    }
}
