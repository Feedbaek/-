package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.ChargeHistoryDto;
import seoul.AutoEveryDay.dto.ChargeSpotDto;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.CarManageService;
import seoul.AutoEveryDay.service.GasStationService;
import seoul.AutoEveryDay.service.LoginService;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('GAS_STATION')")
@RequestMapping("/gasStation")
public class GasStationController {
    private final LoginService loginService;
    private final CarManageService carManageService;
    private final GasStationService gasStationService;
    @GetMapping("")
    public String gasStation() {
        return "gasStation";
    }

    @ResponseBody
    @GetMapping("/history")
    public JsonBody gasStationGet(@RequestParam Long id) {
        return JsonBody.builder()
                .message("주유소 이용 정보 조회 성공")
                .data(gasStationService.getChargeHistory(id))
                .build();
    }

    @ResponseBody
    @PostMapping("/history")
    public JsonBody gasStationPost(@Validated ChargeHistoryDto chargeHistoryDto) {
        User user = loginService.getLoginUser();
        Car car = carManageService.getCar(chargeHistoryDto.getCarId());
        return JsonBody.builder()
                .message("주유소 이용 정보 추가 성공")
                .data(gasStationService.addChargeHistory(chargeHistoryDto, user, car))
                .build();
    }

    @ResponseBody
    @DeleteMapping("/history")
    public JsonBody gasStationDelete(@RequestParam Long id) {
        return JsonBody.builder()
                .message("주유소 이용 정보 삭제 성공")
                .data(gasStationService.deleteChargeHistory(id))
                .build();
    }

    // 이 아래는 주유소 관리 페이지
    @GetMapping("/manage")
    public String gasStationManageGet() {
        return "gasStationManage";
    }

    @ResponseBody
    @PostMapping("/manage")
    public JsonBody gasStationManagePost(@Validated ChargeSpotDto chargeSpotDto) {
        return JsonBody.builder()
                .message("주유구 추가 성공")
                .data(gasStationService.addChargeSpot(chargeSpotDto))
                .build();

    }

    @ResponseBody
    @DeleteMapping("/manage")
    public JsonBody gasStationManageDelete(@RequestParam Long id) {
        return JsonBody.builder()
                .message("주유구 삭제 성공")
                .data(gasStationService.deleteChargeSpot(id))
                .build();
    }

}
