package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.NewCarReq;
import seoul.AutoEveryDay.service.CarManageService;

@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    private final CarManageService carManageService;
    /* 차량 대여 관련 기능 */
    @GetMapping("/rental")
    public String rental() {
        return "carRental";
    }

    @PreAuthorize("hasAuthority('CAR_RENTAL')")
    @PostMapping("/rental/submit")
    @ResponseBody
    public String rentalPost() {
        return "success";
    }

    @GetMapping("/rental/return")
    public String returnCar() {
        return "carReturn";
    }

    /* 차량 관리 기능*/
    @GetMapping("/manage")
    public String manage() {
        return "carManage";
    }

    @ResponseBody
    @PostMapping("/manage/new")
    public ResponseEntity<String> addCar(NewCarReq newCarReq) {
        try {
            return carManageService.createCar(newCarReq);
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
