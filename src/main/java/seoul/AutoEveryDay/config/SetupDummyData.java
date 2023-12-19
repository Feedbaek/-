package seoul.AutoEveryDay.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.CarModel;
import seoul.AutoEveryDay.entity.ChargeSpot;
import seoul.AutoEveryDay.entity.Track;
import seoul.AutoEveryDay.repository.CarModelRepository;
import seoul.AutoEveryDay.repository.CarRepository;
import seoul.AutoEveryDay.repository.ChargeSpotRepository;
import seoul.AutoEveryDay.repository.TrackRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDummyData {
    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;
    private final TrackRepository trackRepository;
    private final ChargeSpotRepository chargeSpotRepository;

    @PostConstruct
    @Transactional
    public void setupCarDummy() {  // 차량 더미 데이터 생성
        { // 아반떼 10대
            CarModel carModel = CarModel.builder()
                    .name("아반떼")
                    .image("/image/car/1.jpeg")
                    .build();
            List<Car> cars = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                cars.add(Car.builder()
                        .number("11가_111" + i)
                        .status("정상")
                        .carModel(carModel)
                        .comment("코멘트" + i)
                        .build());
            }
            carModel.setCars(cars);
            carModelRepository.save(carModel);
        }

        { // 쏘나타 10대
            CarModel carModel = CarModel.builder()
                    .name("쏘나타")
                    .image("/image/car/2.jpeg")
                    .build();
            List<Car> cars = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                cars.add(Car.builder()
                        .number("11가_222" + i)
                        .status("정상")
                        .carModel(carModel)
                        .comment("코멘트" + i)
                        .build());
            }
            carModel.setCars(cars);
            carModelRepository.save(carModel);
        }

        { // 스포티지 10대
            CarModel carModel = CarModel.builder()
                    .name("스포티지")
                    .image("/image/car/3.jpeg")
                    .build();
            List<Car> cars = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                cars.add(Car.builder()
                        .number("11가_333" + i)
                        .status("정상")
                        .carModel(carModel)
                        .comment("코멘트" + i)
                        .build());
            }
            carModel.setCars(cars);
            carModelRepository.save(carModel);
        }

        { // 그랜저 10대
            CarModel carModel = CarModel.builder()
                    .name("그랜저")
                    .image("/image/car/4.jpeg")
                    .build();
            List<Car> cars = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                cars.add(Car.builder()
                        .number("22가_222" + i)
                        .status("정상")
                        .carModel(carModel)
                        .comment("코멘트" + i)
                        .build());
            }
            carModel.setCars(cars);
            carModelRepository.save(carModel);
        }

        { // 제네시스 10대
            CarModel carModel = CarModel.builder()
                    .name("제네시스")
                    .image("/image/car/5.jpeg")
                    .build();
            List<Car> cars = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                cars.add(Car.builder()
                        .number("33가_111" + i)
                        .status("정상")
                        .carModel(carModel)
                        .comment("코멘트" + i)
                        .build());
            }
            carModel.setCars(cars);
            carModelRepository.save(carModel);
        }
    }

    @PostConstruct
    @Transactional
    public void setupTestTrack() { // 테스트 트랙 더미
        for (int i = 0; i < 5; i++) {
            char ch = (char) ('A' + i);
            trackRepository.save(Track.builder()
                    .name("트랙" + ch)
                    .description("테스트 트랙 설명" + i)
                    .build());
        }
    }

    @PostConstruct
    @Transactional
    public void setupChargeSpot() { // 주유구 더미
        for (int i = 0; i < 10; i++) {
            chargeSpotRepository.save(ChargeSpot.builder()
                    .name("주유구" + i)
                    .build());
        }
    }
}
