package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.CarInfo;
import seoul.AutoEveryDay.dto.EditCarReq;
import seoul.AutoEveryDay.dto.NewCarReq;
import seoul.AutoEveryDay.dto.RentCarReq;
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
    @GetMapping("")
    public String getCar(Model model) {
        List<CarInfo> allCar = carManageService.getAllCar();
        model.addAttribute("carList", allCar);
        return "carManage";
    }
    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<String> newCar(NewCarReq newCarReq) {
        return ResponseEntity.ok(carManageService.createCar(newCarReq));
    }
    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @ResponseBody
    @PutMapping("")
    public ResponseEntity<String> editCar(EditCarReq editCarReq) {
        return ResponseEntity.ok(carManageService.updateCar(editCarReq));
    }
    @PreAuthorize("hasAuthority('CAR_MANAGE')")
    @ResponseBody
    @DeleteMapping("")
    public ResponseEntity<String> deleteCar(@RequestParam String number) {
        return ResponseEntity.ok(carManageService.deleteCar(number));
    }
    @GetMapping("/rental")
    public String rentalGet(Model model) {
        List<CarInfo> carInfoList = carManageService.getAllCar();
        model.addAttribute("carList", carInfoList);
        Map<String, List<RentalHistory>> rentalHistoryMap = carRentalService.getRentalHistory(carInfoList);
        model.addAttribute("rentalListMap", rentalHistoryMap);
        return "carRental";
    }
    @PreAuthorize("hasAuthority('CAR_RENTAL')")
    @PostMapping("/rental")
    @ResponseBody
    public ResponseEntity<String> rentalPost(RentCarReq rentCarReq) {
        User user = userService.findByName(LoginService.getAuthenticatedUsername());
        carRentalService.rentCar(rentCarReq, user);
        return ResponseEntity.ok("success");
    }
}
