package seoul.AutoEveryDay.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.repository.CarRepository;

@Component
@RequiredArgsConstructor
public class SetupDummy {
    private final CarRepository carRepository;

    @PostConstruct
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
                    .number("33가_333" + i)
                    .type("제네시스")
                    .status("정상")
                    .comment("코멘트" + i)
                    .build());
        }
    }
}
