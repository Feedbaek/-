package seoul.AutoEveryDay.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.repository.CarRepository;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("차량 관리 서비스 테스트")
public class CarManageServiceTest {
    @InjectMocks
    private CarManageService carManageService;
    @Mock
    private CarRepository carRepository;

    private CarDto makeNewCarReq() {
        return CarDto.builder()
                .number("12가1234")
                .type("아반떼")
                .status("정상")
                .comment("테스트")
                .build();
    }

    @Test
    @DisplayName("차량 등록 성공 테스트")
    public void createCarSuccess() {
        // given
        CarDto newCarReq = makeNewCarReq();
        given(carRepository.existsByNumber(newCarReq.getNumber())).willReturn(false);
        given(carRepository.save(Mockito.any(Car.class))).willReturn(Mockito.any(Car.class));
        // when
        ResponseEntity<?> res = ResponseEntity.ok(carManageService.createCar(newCarReq));
        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("차량 등록 중복 테스트")
    public void createCarFail() {
        // given
        CarDto newCarReq = makeNewCarReq();
        given(carRepository.existsByNumber(newCarReq.getNumber())).willReturn(true);
        // when
        Throwable thrown = catchThrowable(() -> carManageService.createCar(newCarReq));
        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"이미 존재하는 차량 번호입니다.\"");
    }

    @Test
    @DisplayName("차량 정보 조회 성공 테스트")
    public void getCarSuccess() {
        // given
        String number = "12가1234";
        given(carRepository.findByNumber(number)).willReturn(
                java.util.Optional.of(Car.builder()
                        .number(number)
                        .status("정상")
                        .comment("테스트")
                        .build()));
        // when
        ResponseEntity<CarDto> res = ResponseEntity.ok(carManageService.getCar(number));
        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(res.getBody()).getNumber()).isEqualTo(number);
    }

    @Test
    @DisplayName("차량 정보 조회 실패 테스트")
    public void getCarFail() {
        // given
        String number = "12가1234";
        given(carRepository.findByNumber(number)).willReturn(
                Optional.empty());
        // when
        Throwable thrown = catchThrowable(() -> carManageService.getCar(number));
        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"존재하지 않는 차량 번호입니다.\"");
    }

    @Test
    @DisplayName("차량 정보 수정 성공 테스트")
    public void updateCarSuccess() {
        // given
        String number = "12가1234";
        CarDto carDto = CarDto.builder()
                .number(number)
                .status("정상")
                .comment("테스트")
                .build();
        given(carRepository.findByNumber(number)).willReturn(Optional.of(Car.builder()
                .number(number)
                .status("비정상")
                .comment("테스트 전")
                .build()));
        // when
        ResponseEntity<?> res = ResponseEntity.ok(carManageService.updateCar(carDto));
        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo("차량 정보 수정 성공");
    }

    @Test
    @DisplayName("차량 정보 수정 실패 테스트")
    public void updateCarFail() {
        // given
        String number = "12가1234";
        CarDto carDto = CarDto.builder()
                .number(number)
                .status("정상")
                .comment("테스트")
                .build();
        given(carRepository.findByNumber(number)).willReturn(Optional.empty());
        // when
        Throwable thrown = catchThrowable(() -> carManageService.updateCar(carDto));
        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"존재하지 않는 차량 번호입니다.\"");
    }

    @Test
    @DisplayName("차량 삭제 성공 테스트")
    public void deleteCarSuccess() {
        // given
        String number = "12가1234";
        given(carRepository.findByNumber(number)).willReturn(Optional.of(Car.builder()
                .number(number)
                .status("정상")
                .comment("테스트")
                .build()));
        // when
        ResponseEntity<?> res = ResponseEntity.ok(carManageService.deleteCar(number));
        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo("차량 삭제 성공");
    }

    @Test
    @DisplayName("차량 삭제 실패 테스트")
    public void deleteCarFail() {
        // given
        String number = "12가1234";
        given(carRepository.findByNumber(number)).willReturn(Optional.empty());
        // when
        Throwable thrown = catchThrowable(() -> carManageService.deleteCar(number));
        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"존재하지 않는 차량 번호입니다.\"");
    }
}
