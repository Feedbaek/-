package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.dto.TestCenterDto;
import seoul.AutoEveryDay.dto.TestHistoryDto;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.LoginService;
import seoul.AutoEveryDay.service.TestCenterService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/center")
public class TestCenterController { // 테스트 센터 관련 컨트롤러
    private final TestCenterService testCenterService;
    private final LoginService userService;

    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @GetMapping("")  // 테스트 센터 페이지
    public String allCenterGet(Model model) {
        model.addAttribute("testCenterList", testCenterService.getAllTestCenter());
        return "centerManage";
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/manage")  // 테스트 센터 상세 조회
    public JsonBody centerGet(@RequestParam String name) {
        return JsonBody.builder()
                .message("테스트 센터 조회 성공")
                .data(testCenterService.getTestCenter(name))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/manage")  // 테스트 센터 등록
    public JsonBody managePost(TestCenterDto testCenterDto) {
        return JsonBody.builder()
                .message("테스트 센터 등록 성공")
                .data(testCenterService.createTestCenter(testCenterDto))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping("/manage")  // 테스트 센터 수정
    public JsonBody manageEdit(TestCenterDto testCenterDto) {
        return JsonBody.builder()
                .message("테스트 센터 수정 성공")
                .data(testCenterService.editTestCenter(testCenterDto))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping("/manage")  // 테스트 센터 삭제
    public JsonBody manageDelete(@RequestParam String name) {
        return JsonBody.builder()
                .message("테스트 센터 삭제 성공")
                .data(testCenterService.deleteTestCenter(name))
                .build();
    }


    // 여기부터 아래는 예약 관련 컨트롤러

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @PostMapping("/reserve")  // 테스트 센터 예약
    public JsonBody reservePost(TestHistoryDto testHistoryDto) {
        User user = userService.findByName(LoginService.getAuthenticatedUsername());
        return JsonBody.builder()
                .message("예약 성공")
                .data(testCenterService.createTestHistory(testHistoryDto, user))
                .build();
    }
    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @DeleteMapping("/reserve")  // 테스트 센터 예약 취소
    public JsonBody reserveDelete(@RequestParam TestHistoryDto testHistoryDto) {
        User user = userService.findByName(LoginService.getAuthenticatedUsername());
        return JsonBody.builder()
                .message("예약 취소 성공")
                .data(testCenterService.deleteTestHistory(testHistoryDto, user))
                .build();
    }
}
