package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.*;
import seoul.AutoEveryDay.entity.RentalHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.CarManageService;
import seoul.AutoEveryDay.service.CarRentalService;
import seoul.AutoEveryDay.service.LoginService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    private final LoginService userService;
    private final CarManageService carManageService;
    private final CarRentalService carRentalService;

    /* 차량 관리 */

    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @GetMapping("/manage")  // 차량 관리 페이지
    public String getCar(Model model) {
        List<CarInfo> allCar = carManageService.getAllCar();
        model.addAttribute("carList", allCar);
        return "carManage";
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @PostMapping("/manage") // 새로운 차량 등록
    public JsonBody newCar(NewCarReq newCarReq) {
        return JsonBody.builder()
                .message("차량 등록 성공")
                .data(carManageService.createCar(newCarReq)).build();
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @PutMapping("/manage")  // 차량 정보 수정
    public JsonBody editCar(EditCarReq editCarReq) {
        return JsonBody.builder()
                .message("차량 정보 수정 성공")
                .data(carManageService.updateCar(editCarReq)).build();
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @DeleteMapping("/manage")   // 차량 삭제
    public JsonBody deleteCar(@RequestParam String number) {
        return JsonBody.builder()
                .message("차량 삭제 성공")
                .data(carManageService.deleteCar(number)).build();
    }

    /* 차량 대여 */

    @PreAuthorize("hasAuthority('CAR_RENTAL')")
    @GetMapping("/rental")  // 차량 대여 페이지
    public String rentalGet(Model model) {
        List<CarInfo> carInfoList = carManageService.getAllCar();
        model.addAttribute("carList", carInfoList);
        Map<String, List<RentalHistory>> rentalHistoryMap = carRentalService.getRentalHistory(carInfoList);
        model.addAttribute("rentalListMap", rentalHistoryMap);
        return "carRental";
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('CAR_RENTAL')")
    @PostMapping("/rental") // 차량 대여 신청
    public JsonBody rentalPost(RentCarReq rentCarReq) {
        User user = userService.findByName(LoginService.getAuthenticatedUsername());
        return JsonBody.builder()
                .message("차량 대여 성공")
                .data(carRentalService.rentCar(rentCarReq, user)).build();
    }
}
