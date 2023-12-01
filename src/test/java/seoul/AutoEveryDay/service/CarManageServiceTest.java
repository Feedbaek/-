package seoul.AutoEveryDay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.NewCarReq;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.repository.CarRepository;

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

    private NewCarReq makeNewCarReq() {
        return NewCarReq.builder()
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
        NewCarReq newCarReq = makeNewCarReq();
        given(carRepository.existsByNumber(newCarReq.getNumber())).willReturn(false);
        given(carRepository.save(Mockito.any(Car.class))).willReturn(Mockito.any(Car.class));
        // when
        ResponseEntity<String> res = carManageService.createCar(newCarReq);
        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("차량 등록 중복 테스트")
    public void createCarFail() {
        // given
        NewCarReq newCarReq = makeNewCarReq();
        given(carRepository.existsByNumber(newCarReq.getNumber())).willReturn(true);
        // when
        Throwable thrown = catchThrowable(() -> carManageService.createCar(newCarReq));
        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"이미 존재하는 차량 번호입니다.\"");
    }
}
