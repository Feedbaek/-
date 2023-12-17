package seoul.AutoEveryDay.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.dto.RentCarDto;
import seoul.AutoEveryDay.entity.*;
import seoul.AutoEveryDay.repository.RentalHistoryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static seoul.AutoEveryDay.service.CarManageServiceTest.*;
import static seoul.AutoEveryDay.service.LoginServiceTest.makeUser;
import static seoul.AutoEveryDay.service.LoginServiceTest.makeUserGroup;

@ExtendWith(MockitoExtension.class)
@DisplayName("차량 대여 서비스 테스트")
public class CarRentalServiceTest {
    @InjectMocks
    private CarRentalService carRentalService;
    @Mock
    private RentalHistoryRepository rentalHistoryRepository;

    public static RentCarDto makeRentCarDto(Car car) {
        return RentCarDto.builder()
                .id(1L)
                .carId(car.getId())
                .pickupDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .build();
    }

    public static RentCarDto makeRentCarDto() {
        return RentCarDto.builder()
                .id(1L)
                .carId(1L)
                .pickupDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .build();
    }

    public static RentalHistory makeRentalHistory(User user, Car car) {
        return RentalHistory.builder()
                .id(1L)
                .user(user)
                .car(car)
                .pickupDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .build();
    }

    public static RentalHistory makeRentalHistory() {
        return RentalHistory.builder()
                .id(1L)
                .user(makeUser(makeUserGroup()))
                .car(makeCar(makeCarModel()))
                .pickupDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .build();
    }


    @Test
    @DisplayName("차량 대여 성공")
    public void rentCar() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);

        given(rentalHistoryRepository.findByCar_IdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual
                (makeRentCarDto().getCarId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(List.of()); // 대여 기록이 없는 상태
        given(rentalHistoryRepository.save(Mockito.any(RentalHistory.class))).willReturn(makeRentalHistory()); // 대여 기록 저장 성공

        // when
        RentCarDto res = carRentalService.rentCar(rentCarDto, user, car);

        // then
        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(rentCarDto.getId());
        assertThat(res.getCarId()).isEqualTo(rentCarDto.getCarId());
        assertThat(res.getPickupDate()).isEqualTo(rentCarDto.getPickupDate());
        assertThat(res.getReturnDate()).isEqualTo(rentCarDto.getReturnDate());
    }

    @Test
    @DisplayName("차량 대여 실패 - 이미 대여된 차량")
    public void rentCarFail() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        RentalHistory rentalHistory = makeRentalHistory(user, car);

        given(rentalHistoryRepository.findByCar_IdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual
                (rentCarDto.getCarId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(List.of(rentalHistory)); // 대여 기록이 있는 상태

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.rentCar(rentCarDto, user, car));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"이미 대여된 차량입니다.\"");
    }

    @Test
    @DisplayName("차량 대여 실패 - 대여일이 오늘 이전")
    public void rentCarFail2() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        rentCarDto.setPickupDate(LocalDate.now().minusDays(1));

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.rentCar(rentCarDto, user, car));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"대여는 오늘부터 가능합니다.\"");
    }

    @Test
    @DisplayName("차량 대여 실패 - 반납일이 대여일보다 빠르거나 같음")
    public void rentCarFail3() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        rentCarDto.setReturnDate(rentCarDto.getPickupDate());

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.rentCar(rentCarDto, user, car));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"반납일이 대여일보다 빠르거나 같을 수 없습니다.\"");
    }

    @Test
    @DisplayName("차량 대여 실패 - 대여일로부터 7일 이후 반납")
    public void rentCarFail4() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        rentCarDto.setReturnDate(rentCarDto.getPickupDate().plusDays(9));

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.rentCar(rentCarDto, user, car));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"7일 이내로만 대여할 수 있습니다.\"");
    }

    @Test
    @DisplayName("차량 반납 성공")
    public void returnCar() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        RentalHistory rentalHistory = makeRentalHistory();

        given(rentalHistoryRepository.findByUser_IdAndCarIdAndPickupDateAndReturnDate
                (user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(Optional.of(rentalHistory)); // 대여 기록이 있는 상태

        // when
        RentCarDto res = carRentalService.returnCar(rentCarDto, user, car);

        // then
        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(rentCarDto.getId());
        assertThat(res.getCarId()).isEqualTo(car.getId());
        assertThat(res.getPickupDate()).isEqualTo(rentCarDto.getPickupDate());
        assertThat(res.getReturnDate()).isEqualTo(LocalDate.now());  // 반납 완료시 오늘 날짜로 설정
    }

    @Test
    @DisplayName("차량 반납 실패 - 대여 기록이 없음")
    public void returnCarFail() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);

        given(rentalHistoryRepository.findByUser_IdAndCarIdAndPickupDateAndReturnDate
                (user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(Optional.empty()); // 대여 기록이 없는 상태

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.returnCar(rentCarDto, user, car));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"대여 기록이 없습니다.\"");
    }

    @Test
    @DisplayName("차량 반납 실패 - 이미 반납된 차량")
    public void returnCarFail2() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        RentalHistory rentalHistory = makeRentalHistory();
        rentalHistory.setReturnDate(LocalDate.now().minusDays(1));

        given(rentalHistoryRepository.findByUser_IdAndCarIdAndPickupDateAndReturnDate
                (user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(Optional.of(rentalHistory)); // 대여 기록이 있는 상태

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.returnCar(rentCarDto, user, makeCar()));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"이미 반납된 차량입니다.\"");
    }

    @Test
    @DisplayName("차량 반납 실패 - 아직 대여일이 되지 않음")
    public void returnCarFail3() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto();
        RentalHistory rentalHistory = makeRentalHistory();
        rentalHistory.setPickupDate(LocalDate.now().plusDays(1));

        given(rentalHistoryRepository.findByUser_IdAndCarIdAndPickupDateAndReturnDate
                (user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(Optional.of(rentalHistory)); // 대여 기록이 있는 상태

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.returnCar(rentCarDto, user, makeCar()));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"아직 대여일이 되지 않은 차량입니다.\"");
    }
    @Test
    @DisplayName("차량 대여 내역 조회 - 오늘 이후로만 조회")
    public void getRentalHistory() {
        // given
        User user = makeUser();
        Car car = makeCar();
        Long carId = car.getId();
        RentalHistory rentalHistory = makeRentalHistory();
        List<CarDto> carDtoList = List.of(makeCarDto());

        given(rentalHistoryRepository.findByCar_IdAndReturnDateGreaterThanEqual
                (carId, LocalDate.now())).willReturn(List.of(rentalHistory)); // 대여 기록이 있는 상태
        // when
        Map<Long, List<RentalHistory>> res = carRentalService.getRentalHistory(carDtoList);

        // then
        assertThat(res).isNotNull();
        assertThat(res.get(carId)).isNotNull();
        assertThat(res.get(carId).get(0).getId()).isEqualTo(rentalHistory.getId());
        assertThat(res.get(carId).get(0).getUser().getId()).isEqualTo(user.getId());
        assertThat(res.get(carId).get(0).getCar().getId()).isEqualTo(car.getId());
        assertThat(res.get(carId).get(0).getPickupDate()).isEqualTo(rentalHistory.getPickupDate());
        assertThat(res.get(carId).get(0).getReturnDate()).isEqualTo(rentalHistory.getReturnDate());
    }

    @Test
    @DisplayName("차량 대여 취소 성공")
    public void deleteRental() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);
        RentalHistory rentalHistory = makeRentalHistory();

        given(rentalHistoryRepository.findByUser_IdAndCarIdAndPickupDateAndReturnDate
                (user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(Optional.of(rentalHistory)); // 대여 기록이 있는 상태

        // when
        RentCarDto res = carRentalService.deleteRental(rentCarDto, user, car);

        // then
        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(rentCarDto.getId());
        assertThat(res.getCarId()).isEqualTo(car.getId());
        assertThat(res.getPickupDate()).isEqualTo(rentCarDto.getPickupDate());
        assertThat(res.getReturnDate()).isEqualTo(rentCarDto.getReturnDate());
    }

    @Test
    @DisplayName("차량 대여 취소 실패 - 대여 기록이 없음")
    public void deleteRentalFail() {
        // given
        User user = makeUser();
        Car car = makeCar();
        RentCarDto rentCarDto = makeRentCarDto(car);

        given(rentalHistoryRepository.findByUser_IdAndCarIdAndPickupDateAndReturnDate
                (user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate())).willReturn(Optional.empty()); // 대여 기록이 없는 상태

        // when
        Throwable thrown = catchThrowable(() -> carRentalService.deleteRental(rentCarDto, user, car));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class).hasMessage("400 BAD_REQUEST \"대여 기록이 없습니다.\"");
    }
}
