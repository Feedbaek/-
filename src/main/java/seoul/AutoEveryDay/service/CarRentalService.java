package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.CarAvailableDate;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.dto.RentCarDto;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.RentalHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.RentalHistoryRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "CarRentalService")
@Transactional
@RequiredArgsConstructor
public class CarRentalService {
    private final RentalHistoryRepository rentalHistoryRepository;

    private void validatePickUpDateAndReturnDate(RentCarDto rentCarDto) {
        if (LocalDate.now().isAfter(rentCarDto.getPickupDate())) {
            log.error("대여는 오늘부터 가능합니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여는 오늘부터 가능합니다.");
        }
        if (!rentCarDto.getPickupDate().isBefore(rentCarDto.getReturnDate())) {
            log.error("반납일이 대여일보다 빠르거나 같을 수 없습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "반납일이 대여일보다 빠르거나 같을 수 없습니다.");
        }
        if (rentCarDto.getReturnDate().isAfter(LocalDate.now().plusDays(21))) {
            log.error("21일 이내로만 신청할 수 있습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "21 이내로만 신청할 수 있습니다.");
        }
        if (!rentCarDto.getPickupDate().isAfter(rentCarDto.getReturnDate().minusDays(8))) {
            log.error("7일 이내로만 대여할 수 있습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "7일 이내로만 대여할 수 있습니다.");
        }
    }

    /**
     * <h3>차량 대여.</h3>
     * 차량 번호가 존재하지 않으면, 이미 대여된 차량이면, 대여일이 오늘 이전이면,
     * 반납일이 대여일보다 빠르거나 같으면, 대여일로부터 7일 이후 반납이면 ResponseStatusException 발생
     */
    public RentCarDto rentCar(RentCarDto rentCarDto, User user, Car car) {
        if (!rentalHistoryRepository.findByCarIdAndReturnDateGreaterThanAndPickupDateLessThan(car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate()).isEmpty()) {
            log.error("이미 대여된 차량입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 대여된 차량입니다.");
        }

        validatePickUpDateAndReturnDate(rentCarDto);

        RentalHistory rentalHistory = RentalHistory.builder()
                .car(car)
                .user(user)
                .pickupDate(rentCarDto.getPickupDate())
                .returnDate(rentCarDto.getReturnDate())
                .build();
        try {
            rentalHistoryRepository.save(rentalHistory);
        } catch (Exception e) {
            log.error("대여 기록 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "대여 기록 저장 실패");
        }
        return rentCarDto;
    }

    /**
     * <h3>차량 반납.</h3>
     * 차량 번호가 존재하지 않으면, 대여 기록이 없으면 ResponseStatusException 발생
     */
    public RentCarDto returnCar(RentCarDto rentCarDto, User user, Car car) {
        RentalHistory rentalHistory = rentalHistoryRepository
                .findByUserIdAndCarIdAndPickupDateAndReturnDate(user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여 기록이 없습니다."));
        if (rentalHistory.getReturnDate().isBefore(LocalDate.now())) {
            log.error("이미 반납된 차량입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 반납된 차량입니다.");
        }
        if (rentalHistory.getPickupDate().isAfter(LocalDate.now())) {
            log.error("아직 대여하지 않은 차량입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아직 대여일이 되지 않은 차량입니다.");
        }

        rentalHistory.setReturnDate(LocalDate.now());
        return RentCarDto.builder()
                .id(rentalHistory.getId())
                .carId(car.getId())
                .pickupDate(rentalHistory.getPickupDate())
                .returnDate(rentalHistory.getReturnDate())
                .build();
    }

    /**
     * <h3>차량 대여 내역.</h3>
     * 오늘 이후 날짜로 차량 예약 내역을 반환
     */
    public Map<Long, List<RentalHistory>> getRentalHistory(List<CarDto> carDtoList) {
        Map<Long, List<RentalHistory>> rentalHistoryMap = new HashMap<>();

        carDtoList.forEach(carDto -> {
            Long id = carDto.getId();
            rentalHistoryMap.put(id, rentalHistoryRepository.findByCarIdAndReturnDateGreaterThan(id, LocalDate.now()));
        });

        return rentalHistoryMap;
    }

    /**
     * <h3>차량 대여 취소.</h3>
     * 차량 번호가 존재하지 않으면, 대여 기록이 없으면 ResponseStatusException 발생
     */
    public RentCarDto deleteRental(RentCarDto rentCarDto, User user, Car car) {
        RentalHistory rentalHistory = rentalHistoryRepository.findByUserIdAndCarIdAndPickupDateAndReturnDate(user.getId(), car.getId(), rentCarDto.getPickupDate(), rentCarDto.getReturnDate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "대여 기록이 없습니다."));

        try {
            rentalHistoryRepository.delete(rentalHistory);
        } catch (Exception e) {
            log.error("대여 기록 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "대여 기록 삭제 실패");
        }
        return RentCarDto.builder()
                .id(rentalHistory.getId())
                .carId(car.getId())
                .pickupDate(rentalHistory.getPickupDate())
                .returnDate(rentalHistory.getReturnDate())
                .build();
    }

    public List<List<CarAvailableDate>> getAvailableDate(Car car) {
        List<RentalHistory> unavailableDate = rentalHistoryRepository.findByCarIdAndReturnDateGreaterThan(
                car.getId(), LocalDate.now());
        List<List<CarAvailableDate>> availableDate = new ArrayList<>();

        // 3주치 날짜를 미리 만들어놓고, 대여 기록이 있는 날짜는 제거
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            availableDate.add(new ArrayList<>());
            for (int j = 0; j < 7; j++) {
                LocalDate date = now.plusDays(i * 7 + j);

                CarAvailableDate carAvailableDate = CarAvailableDate.builder()
                        .day(date.getDayOfMonth())
                        .available(true)
                        .build();

                if (i >= 3) {
                    carAvailableDate.setAvailable(false);
                    availableDate.get(i).add(carAvailableDate);
                    continue;
                }

                // 대여 기록이 있는 날짜를 제거
                for (RentalHistory rentalHistory : unavailableDate) {
                    if ((date.isEqual(rentalHistory.getPickupDate()) || date.isAfter(rentalHistory.getPickupDate()))
                            && date.isBefore(rentalHistory.getReturnDate())) {
                        carAvailableDate.setAvailable(false);
                        break;
                    }
                }
                availableDate.get(i).add(carAvailableDate);
            }
        }

        return availableDate;
    }
}
