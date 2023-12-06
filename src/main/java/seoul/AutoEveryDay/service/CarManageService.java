package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.repository.CarRepository;
import seoul.AutoEveryDay.repository.RentalHistoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "CarManageService")
@Transactional
@RequiredArgsConstructor
public class CarManageService {
    private final CarRepository carRepository;
    private final RentalHistoryRepository rentalHistoryRepository;

    private Car save(Car car) {
        try {
            return carRepository.save(car);
        } catch (Exception e) {
            log.error("차량 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "차량 저장 실패");
        }
    }

    private void delete(Car car) {
        try {
            carRepository.delete(car);
        } catch (Exception e) {
            log.error("차량 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "차량 삭제 실패");
        }
    }

    /** <h3>차량 등록.</h3>
     * 이미 존재하는 차량 번호면 ResponseStatusException 발생 */
    public CarDto createCar(CarDto carDto) {
        // todo: 차량 번호 정규식 검사, 차량 종류 검사, 차량 상태 검사
        if (carRepository.existsByNumber(carDto.getNumber())) {
            log.error("이미 존재하는 차량 번호입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 차량 번호입니다.");
        }
        Car car = Car.builder()
                .number(carDto.getNumber())
                .type(carDto.getType())
                .status(carDto.getStatus())
                .comment(carDto.getComment())
                .build();
        save(car);

        return carDto;
    }

    /** <h3>차량 정보 조회.</h3>
     * 차량 번호가 존재하지 않으면 ResponseStatusException 발생 */
    public CarDto getCar(String number) {
        Car car = carRepository.findByNumber(number).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));

        return CarDto.builder()
                .number(car.getNumber())
                .type(car.getType())
                .status(car.getStatus())
                .comment(car.getComment())
                .build();
    }

    /** <h3>모든 차량 정보 조회.</h3> */
    public List<CarDto> getAllCar() {
        List<Car> carList = carRepository.findAll();
        List<CarDto> carDtoList = new ArrayList<>();

        carList.forEach(car -> {
            carDtoList.add(CarDto.builder()
                    .number(car.getNumber())
                    .type(car.getType())
                    .status(car.getStatus())
                    .comment(car.getComment())
                    .build());
        });

        return carDtoList;
    }

    /** <h3>차량 정보 수정.</h3>
     * 차량 번호가 존재하지 않으면 ResponseStatusException 발생 */
    public CarDto updateCar(CarDto carDto) {
        Car car = carRepository.findByNumber(carDto.getNumber()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));
        car.setStatus(carDto.getStatus());
        car.setComment(carDto.getComment());

        return CarDto.builder()
                .number(car.getNumber())
                .type(car.getType())
                .status(car.getStatus())
                .comment(car.getComment())
                .build();
    }

    /** <h3>차량 삭제.</h3>
     * 차량 번호가 존재하지 않으면 ResponseStatusException 발생 */
    public CarDto deleteCar(String number) {
        Car car = carRepository.findByNumber(number).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 차량 번호입니다."));
        delete(car);

        return CarDto.builder()
                .number(car.getNumber())
                .type(car.getType())
                .status(car.getStatus())
                .comment(car.getComment())
                .build();
    }
}
