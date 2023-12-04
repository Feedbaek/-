package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.CarInfo;
import seoul.AutoEveryDay.dto.EditCarReq;
import seoul.AutoEveryDay.dto.NewCarReq;
import seoul.AutoEveryDay.dto.RentCarReq;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.repository.CarRepository;
import seoul.AutoEveryDay.repository.RentalHistoryRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "CarManageService")
@Transactional
@RequiredArgsConstructor
public class CarManageService {
    private final CarRepository carRepository;
    private final RentalHistoryRepository rentalHistoryRepository;

    /** <h3>차량 등록.</h3>
     * 이미 존재하는 차량 번호면 ResponseStatusException 발생 */
    public String createCar(NewCarReq newCarReq) {
        // todo: 차량 번호 정규식 검사
        // todo: 차량 종류 검사
        // todo: 차량 상태 검사
        if (carRepository.existsByNumber(newCarReq.getNumber())) {
            log.error("이미 존재하는 차량 번호입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 차량 번호입니다.");
        }
        Car car = Car.builder()
                .number(newCarReq.getNumber())
                .type(newCarReq.getType())
                .status(newCarReq.getStatus())
                .comment(newCarReq.getComment())
                .build();
        carRepository.save(car);

        return "차량 등록 성공";
    }

    /** <h3>차량 정보 조회.</h3>
     * 차량 번호가 존재하지 않으면 ResponseStatusException 발생 */
    public CarInfo getCar(String number) {
        Car car = carRepository.findByNumber(number).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));

        return CarInfo.builder()
                .number(car.getNumber())
                .type(car.getType())
                .status(car.getStatus())
                .comment(car.getComment())
                .build();
    }

    /** <h3>모든 차량 정보 조회.</h3> */
    public List<CarInfo> getAllCar() {
        List<Car> carList = carRepository.findAll();
        List<CarInfo> carInfoList = new ArrayList<>();

        carList.forEach(car -> {
            carInfoList.add(CarInfo.builder()
                    .number(car.getNumber())
                    .type(car.getType())
                    .status(car.getStatus())
                    .comment(car.getComment())
                    .build());
        });

        return carInfoList;
    }

    /** <h3>차량 정보 수정.</h3>
     * 차량 번호가 존재하지 않으면 ResponseStatusException 발생 */
    public String updateCar(EditCarReq editCarReq) {
        Car car = carRepository.findByNumber(editCarReq.getNumber()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));
        car.setStatus(editCarReq.getStatus());
        car.setComment(editCarReq.getComment());
        carRepository.save(car);

        return "차량 정보 수정 성공";
    }

    /** <h3>차량 삭제.</h3>
     * 차량 번호가 존재하지 않으면 ResponseStatusException 발생 */
    public String deleteCar(String number) {
        Car car = carRepository.findByNumber(number).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));
        carRepository.delete(car);

        return "차량 삭제 성공";
    }
}
