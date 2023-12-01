package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.NewCarReq;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.repository.CarRepository;

@Service
@Slf4j(topic = "CarManageService")
@Transactional
@RequiredArgsConstructor
public class CarManageService {
    private final CarRepository carRepository;

    /** <h3>차량 등록.</h3>
     * 이미 존재하는 차량 번호면 ResponseStatusException 발생 */
    public ResponseEntity<String> createCar(NewCarReq newCarReq) {
        if (carRepository.existsByNumber(newCarReq.getNumber())) {
            log.error("이미 존재하는 차량 번호입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 차량 번호입니다.");
        }
        Car car = Car.builder()
                .number(newCarReq.getNumber())
                .type(newCarReq.getType())
                .available(true)
                .status(newCarReq.getStatus())
                .comment(newCarReq.getComment())
                .build();
        carRepository.save(car);

        return ResponseEntity.ok("차량 등록 성공");
    }
}
