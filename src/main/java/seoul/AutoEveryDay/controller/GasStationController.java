package seoul.AutoEveryDay.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('GAS_STATION')")
@RequestMapping("/gasStation")
public class GasStationController {
    private final LoginService loginService;
    private final CarManageService carManageService;
    private final GasStationService gasStationService;
    @GetMapping("/history")
    public String gasStation(Model model) {
        List<List<String>> chargeHistoryData = gasStationService.chargeHistoryData();
        String[] chargeHistoryTitles = {"사용자", "소속", "차량", "주유구", "주유량", "날짜", "삭제"};
        model.addAttribute("chargeHistoryTitles", chargeHistoryTitles);
        model.addAttribute("chargeHistoryList", chargeHistoryData);
        return "gasStationHistory";
    }

    @ResponseBody
    @GetMapping("/history/{id}")
    public JsonBody gasStationGet(@PathVariable("id") Long id) {
        return JsonBody.builder()
                .message("주유소 이용 정보 조회 성공")
                .data(gasStationService.getChargeHistory(id))
                .build();
    }

    @ResponseBody
    @Operation(summary = "주유소 이용 정보 추가")
    @PostMapping("/history")
    public JsonBody gasStationPost(@Validated @RequestBody ChargeHistoryDto chargeHistoryDto) {
        User user = loginService.getLoginUser();
        Car car = carManageService.getCar(chargeHistoryDto.getCarId());
        return JsonBody.builder()
                .message("주유소 이용 정보 추가 성공")
                .data(gasStationService.addChargeHistory(chargeHistoryDto, user, car))
                .build();
    }

    @ResponseBody
    @DeleteMapping("/history/{id}")
    public JsonBody gasStationDelete(@PathVariable("id") Long id) {
        return JsonBody.builder()
                .message("주유소 이용 정보 삭제 성공")
                .data(gasStationService.deleteChargeHistory(id))
                .build();
    }

    // 이 아래는 주유소 관리 페이지
    @GetMapping("/manage")
    public String gasStationManageGet(Model model) {
        List<List<String>> chargeSpotData = gasStationService.chargeSpotData();
        String[] chargeSpotTitles = {"주유구 ID", "주유구", "삭제"};
        model.addAttribute("chargeSpotTitles", chargeSpotTitles);
        model.addAttribute("chargeSpotList", chargeSpotData);
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
    @DeleteMapping("/manage/{id}")
    public JsonBody gasStationManageDelete(@PathVariable("id") Long id) {
        return JsonBody.builder()
                .message("주유구 삭제 성공")
                .data(gasStationService.deleteChargeSpot(id))
                .build();
    }

}
