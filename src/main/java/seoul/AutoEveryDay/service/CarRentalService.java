package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.RentCarReq;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.RentalHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.CarRepository;
import seoul.AutoEveryDay.repository.RentalHistoryRepository;

import java.time.LocalDate;

@Service
@Slf4j(topic = "CarRentalService")
@Transactional
@RequiredArgsConstructor
public class CarRentalService {
    private final CarRepository carRepository;
    private final RentalHistoryRepository rentalHistoryRepository;

    /** <h3>차량 대여.</h3>
     * 차량 번호가 존재하지 않으면, 이미 대여된 차량이면, 대여일이 오늘 이전이면,
     * 반납일이 대여일보다 빠르거나 같으면, 대여일로부터 7일 이후 반납이면 ResponseStatusException 발생 */
    public void rentCar(RentCarReq rentCarReq, User user) {
        Car car = carRepository.findByNumber(rentCarReq.getNumber()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));
        if (LocalDate.now().isAfter(rentCarReq.getPickUpDate())) {
            log.error("대여는 오늘부터 가능합니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여는 오늘부터 가능합니다.");
        }
        if (!rentCarReq.getPickUpDate().isBefore(rentCarReq.getReturnDate())) {
            log.error("반납일이 대여일보다 빠르거나 같을 수 없습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "반납일이 대여일보다 빠르거나 같을 수 없습니다.");
        }
        if (!rentCarReq.getPickUpDate().isAfter(rentCarReq.getReturnDate().minusDays(7))) {
            log.error("7일 이내로만 대여할 수 있습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "7일 이내로만 대여할 수 있습니다.");
        }
        if (rentalHistoryRepository.findByReturnDateGreaterThanAndPickupDateLessThan(rentCarReq.getPickUpDate(), rentCarReq.getReturnDate()).isPresent()) {
            log.error("이미 예약된 차량입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 차량입니다.");
        }

        RentalHistory rentalHistory = RentalHistory.builder()
                .car(car)
                .user(user)
                .pickupDate(rentCarReq.getPickUpDate())
                .returnDate(rentCarReq.getReturnDate())
                .build();
        rentalHistoryRepository.save(rentalHistory);
    }
}
