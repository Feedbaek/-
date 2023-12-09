package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.dto.RentCarReq;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.RentalHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.CarRepository;
import seoul.AutoEveryDay.repository.RentalHistoryRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "CarRentalService")
@Transactional
@RequiredArgsConstructor
public class CarRentalService {
    private final RentalHistoryRepository rentalHistoryRepository;

    private void validatePickUpDateAndReturnDate(RentCarReq rentCarReq) {
        if (LocalDate.now().isAfter(rentCarReq.getPickupDate())) {
            log.error("대여는 오늘부터 가능합니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여는 오늘부터 가능합니다.");
        }
        if (!rentCarReq.getPickupDate().isBefore(rentCarReq.getReturnDate())) {
            log.error("반납일이 대여일보다 빠르거나 같을 수 없습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "반납일이 대여일보다 빠르거나 같을 수 없습니다.");
        }
        if (!rentCarReq.getPickupDate().isAfter(rentCarReq.getReturnDate().minusDays(8))) {
            log.error("7일 이내로만 대여할 수 있습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "7일 이내로만 대여할 수 있습니다.");
        }
    }

    /**
     * <h3>차량 대여.</h3>
     * 차량 번호가 존재하지 않으면, 이미 대여된 차량이면, 대여일이 오늘 이전이면,
     * 반납일이 대여일보다 빠르거나 같으면, 대여일로부터 7일 이후 반납이면 ResponseStatusException 발생
     */
    public RentCarReq rentCar(RentCarReq rentCarReq, User user, Car car) {
        if (!rentalHistoryRepository.findByCarIdAndReturnDateGreaterThanAndPickupDateLessThan(car.getId(), rentCarReq.getPickupDate(), rentCarReq.getReturnDate()).isEmpty()) {
            log.error("이미 대여된 차량입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 대여된 차량입니다.");
        }

        validatePickUpDateAndReturnDate(rentCarReq);

        RentalHistory rentalHistory = RentalHistory.builder()
                .car(car)
                .user(user)
                .pickupDate(rentCarReq.getPickupDate())
                .returnDate(rentCarReq.getReturnDate())
                .build();
        try {
            rentalHistoryRepository.save(rentalHistory);
        } catch (Exception e) {
            log.error("대여 기록 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "대여 기록 저장 실패");
        }
        return rentCarReq;
    }

    /**
     * <h3>차량 반납.</h3>
     * 차량 번호가 존재하지 않으면, 대여 기록이 없으면 ResponseStatusException 발생
     */
    public RentCarReq returnCar(RentCarReq rentCarReq, User user, Car car) {
        RentalHistory rentalHistory = rentalHistoryRepository
                .findByUserIdAndCarIdAndPickupDateAndReturnDate(user.getId(), car.getId(), rentCarReq.getPickupDate(), rentCarReq.getReturnDate()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여 기록이 없습니다."));
        rentalHistory.setReturnDate(LocalDate.now());
        return RentCarReq.builder()
                .number(car.getNumber())
                .pickupDate(rentalHistory.getPickupDate())
                .returnDate(rentalHistory.getReturnDate())
                .build();
    }

    /**
     * <h3>차량 대여 내역.</h3>
     * 오늘 이후 날짜로 차량 예약 내역을 반환
     */
    public Map<String, List<RentalHistory>> getRentalHistory(List<CarDto> carDtoList) {
        Map<String, List<RentalHistory>> rentalHistoryMap = new HashMap<>();

        // todo: 이 함수는 역할 분리가 필요함
//        carDtoList.stream().map(CarDto::getNumber).forEach(number -> {
//            Car car = carRepository.findByNumber(number).orElseThrow(
//                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));
//            rentalHistoryMap.put(car.getNumber(), rentalHistoryRepository.findByCarIdAndReturnDateGreaterThan(car.getId(), LocalDate.now()));
//        });

        return rentalHistoryMap;
    }

    /**
     * <h3>차량 대여 취소.</h3>
     * 차량 번호가 존재하지 않으면, 대여 기록이 없으면 ResponseStatusException 발생
     */
    public RentCarReq deleteRental(RentCarReq rentCarReq, User user, Car car) {
        RentalHistory rentalHistory = rentalHistoryRepository.findByUserIdAndCarIdAndPickupDateAndReturnDate(user.getId(), car.getId(), rentCarReq.getPickupDate(), rentCarReq.getReturnDate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여 기록이 없습니다."));

        try {
            rentalHistoryRepository.delete(rentalHistory);
        } catch (Exception e) {
            log.error("대여 기록 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "대여 기록 삭제 실패");
        }
        return RentCarReq.builder()
                .number(car.getNumber())
                .pickupDate(rentalHistory.getPickupDate())
                .returnDate(rentalHistory.getReturnDate())
                .build();
    }
}
