package seoul.AutoEveryDay.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seoul.AutoEveryDay.entity.*;
import seoul.AutoEveryDay.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static seoul.AutoEveryDay.enums.PrivilegeEnum.READ_PRIVILEGE;
import static seoul.AutoEveryDay.enums.RoleEnum.ROLE_USER;

@Component
@RequiredArgsConstructor
public class SetupDummyData {
    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;
    private final TrackRepository trackRepository;
    private final ChargeSpotRepository chargeSpotRepository;
    private final ChargeHistoryRepository chargeHistoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserGroupRepository userGroupRepository;
    private final DriveHistoryRepository driveHistoryRepository;
    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;
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
                        .comment("코멘트" + (i + 1))
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
                        .comment("코멘트" + (i + 1))
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
                        .comment("코멘트" + (i + 1))
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
                        .comment("코멘트" + (i + 1))
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
                        .comment("코멘트" + (i + 1))
                        .build());
            }
            carModel.setCars(cars);
            carModelRepository.save(carModel);
        }


        // 유저 더미 데이터 생성
        // 현대오토에버 그룹 생성
        UserGroup userGroup = UserGroup.builder()
                .name("현대오토에버")
                .build();
        userGroupRepository.save(userGroup);

        // 기본 권한 생성
        Privilege readPrivilege = Privilege.builder()
                .name(READ_PRIVILEGE.getValue())
                .build();
        privilegeRepository.save(readPrivilege);

        // 유저 역할 생성
        Role role = Role.builder()
                .name(ROLE_USER.getValue())
                .privileges(Set.of(readPrivilege))
                .build();
        roleRepository.save(role);

        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .username("user" + (i + 1))
                    .password(passwordEncoder.encode("1234"))
                    .name("참가자" + (i + 1))
                    .userGroup(userGroup)
                    .roles(Set.of(role))
                    .build();
            userGroup.setUsers(List.of(user));
            userRepository.save(user);
        }

        // 트랙 더미 데이터 생성
        for (int i = 0; i < 5; i++) {
            char ch = (char) ('A' + i);
            String description;
            if (i == 0)
                description = "기본적인 실내 트랙입니다.";
            else if (i == 1)
                description = "코너링이 많은 실내 트랙입니다.";
            else if (i == 2)
                description = "기본적인 실외 트랙입니다.";
            else if (i == 3)
                description = "신호와 장애물이 많은 실내 트랙입니다.";
            else
                description = "사막과 비슷한 환경의 실외 트랙입니다.";
            trackRepository.save(Track.builder()
                    .name("트랙" + ch)
                    .description(description)
                    .image("/image/track/" + (i + 1) + ".jpeg")
                    .build());
        }

        // 주유구 더미 데이터 생성
        for (int i = 0; i < 5; i++) {
            char ch = (char) ('A' + i);
            chargeSpotRepository.save(ChargeSpot.builder()
                    .name("주유구" + ch)
                    .build());
        }

        // 주유 내역 더미 데이터 생성
        for (int i = 0; i < 10; i++) {
            chargeHistoryRepository.save(ChargeHistory.builder()
                    .user(userRepository.findById((long) i+1).orElseThrow())
                    .car(carRepository.findById((long) i+1).orElseThrow())
                    .chargeSpot(chargeSpotRepository.findById(((long) i % 5) + 1).orElseThrow())
                    .amount(10)
                    .build());
        }

        // 주행 내역 더미 데이터 생성
        for (int i = 0; i < 10; i++) {
            driveHistoryRepository.save(DriveHistory.builder()
                    .user(userRepository.findById((long) i+1).orElseThrow())
                    .car(carRepository.findById((long) i+1).orElseThrow())
                    .track(trackRepository.findById(((long) i % 5) + 1).orElseThrow())
                    .distance(10)
                    .time(10)
                    .averageSpeed(60)
                    .maxSpeed(100)
                    .date(LocalDate.now())
                    .build());
        }
    }
}
