package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.*;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.CarManageService;
import seoul.AutoEveryDay.service.CarRentalService;
import seoul.AutoEveryDay.service.LoginService;
import seoul.AutoEveryDay.utils.Converter;

import java.time.LocalDate;
import java.util.List;

@Controller
@PreAuthorize(value = "hasAnyAuthority('READ')")
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    private final LoginService loginService;
    private final CarManageService carManageService;
    private final CarRentalService carRentalService;
    private final Converter converter;

    /* 차량 대여 */
    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @GetMapping("/rental")  // 차량 모델 선택 페이지
    public String rentalGet(Model model) {
        List<String> carModelList = carManageService.getAllCarModelNames();
        for (String carModel : carModelList) {
            System.out.println(carModel);
        }
        model.addAttribute("carModelList", carModelList);
        return "modelSelect";
    }

    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @GetMapping("/rental/{carModel}")  // 차량 대여 페이지
    public String rentalGet(Model model,
                            @PathVariable(value = "carModel", required = false) Long carModelId,
                            @RequestParam(value = "q", required = false) String carNumber) {
        List<CarDto> carDtoList = carManageService.searchCarDto(carModelId, carNumber);
        String[] carInfo = {"차량 번호", "차종", "상태", "메모", "대여/반납"};
        List<List<String>> listList = converter.convertCarDtoList(carDtoList);
        model.addAttribute("carList", listList);
        model.addAttribute("carListTitles", carInfo);
        model.addAttribute("search", carNumber);
        return "carRental";
    }

    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @GetMapping("/rental/{carModel}/{carId}") // 차량 대여 날짜 선택 페이지
    public String rentalGet(@PathVariable("carId") Long carId, Model model) {
        Car car = carManageService.getCar(carId);
        LocalDate now = LocalDate.now();

        List<List<AvailableDate>> dateArr = carRentalService.getAvailableDate(car);
        List<String> dayOfWeek = converter.getDayOfWeek(now);

        model.addAttribute("dateArr", dateArr);
        model.addAttribute("carId", carId);
        return "carRentalDate";
    }

    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @GetMapping("/rental/history") // 차량 대여 내역 페이지
    public String rentalHistoryGet(Model model) {
        User user = loginService.getLoginUser();
        String[] rentalHistoryTitles = {"차량 번호", "차종", "대여일", "반납일", "상태", "취소/반납"};

        List<List<String>> rentalHistoryList = carRentalService.getRentalHistoryList(user);
        model.addAttribute("rentalHistoryTitles", rentalHistoryTitles);
        model.addAttribute("rentalHistoryList", rentalHistoryList);
        return "carRentalHistory";
    }


    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @PostMapping("/rental") // 차량 대여 신청
    public JsonBody rentalPost(@Validated @RequestBody RentCarDto rentCarDto) {
        User user = loginService.getLoginUser();
        Car car = carManageService.getCar(rentCarDto.getCarId());
        return JsonBody.builder()
                .message("차량 대여 성공")
                .data(carRentalService.rentCar(rentCarDto, user, car))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @PostMapping("/rental/return/{historyId}")  // 차량 반납
    public JsonBody rentalPut(@PathVariable("historyId") Long historyId) {
        return JsonBody.builder()
                .message("차량 반납 성공")
                .data(carRentalService.returnCar(historyId))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @PostMapping("/rental/cancel/{historyId}")  // 차량 대여 취소
    public JsonBody rentalCancel(@PathVariable("historyId") Long historyId) {
        return JsonBody.builder()
                .message("차량 대여 취소 성공")
                .data(carRentalService.cancelRental(historyId))
                .build();
    }


    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('CAR_RENTAL')")
    @DeleteMapping("/rental/{historyId}")   // 대여 기록 삭제
    public JsonBody rentalDelete(@PathVariable("historyId") Long historyId) {
        return JsonBody.builder()
                .message("차량 대여 취소 성공")
                .data(carRentalService.deleteRental(historyId))
                .build();
    }

    /* 차량 관리 */
    @GetMapping("/manage")  // 차량 페이지
    public String getCar(Model model, @RequestParam(value = "q", required = false) String carNumber) {
        List<CarDto> carDtoList = carManageService.searchCarDto(carNumber);
        String[] carInfo = {"차량 번호", "차종", "상태", "메모", "수정/삭제"};
        List<List<String>> listList = converter.convertCarDtoList(carDtoList);
        model.addAttribute("carList", listList);
        model.addAttribute("carListTitles", carInfo);
        model.addAttribute("search", carNumber);
        return "carManage";
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/manage") // 새로운 차량 등록
    public JsonBody newCar(@Validated CarDto carDto) {
        return JsonBody.builder()
                .message("차량 등록 성공")
                .data(carManageService.createCar(carDto))
                .build();
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/manage")  // 차량 정보 수정
    public JsonBody editCar(@Validated CarDto carDto) {
        return JsonBody.builder()
                .message("차량 정보 수정 성공")
                .data(carManageService.updateCar(carDto))
                .build();
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/manage")   // 차량 삭제
    public JsonBody deleteCar(@RequestParam("number") String number) {
        return JsonBody.builder()
                .message("차량 삭제 성공")
                .data(carManageService.deleteCar(number))
                .build();
    }
}
