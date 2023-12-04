package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.service.TestCenterService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/center")
public class TestCenterController { // 테스트 센터 관련 컨트롤러
    private final TestCenterService testCenterService;

    // todo: 테스트 센터 관련 컨트롤러 구현

    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @GetMapping("")  // 테스트 센터 페이지
    public String allCenterGet(Model model) {
        model.addAttribute("testCenterList", testCenterService.getAllTestCenter());
        return "centerReserve";
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @GetMapping("{id}")  // 테스트 센터 조회
    public JsonBody centerGet(@PathVariable Long id) {
        return JsonBody.builder()
                .message("테스트 센터 조회 성공")
                .data(testCenterService.getTestCenter(id))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @PostMapping("/reserve")  // 테스트 센터 예약
    public JsonBody reservePost() {
        return JsonBody.builder()
                .message("예약 성공")
                .data(testCenterService.createTestCenter())
                .build();
    }
    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TEST_CENTER')")
    @DeleteMapping("/reserve")  // 테스트 센터 예약 취소
    public JsonBody reserveDelete() {
        return JsonBody.builder()
                .message("예약 취소 성공")
                .data(testCenterService.deleteTestCenter())
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/manage")  // 테스트 센터 등록
    public JsonBody managePost() {
        return JsonBody.builder()
                .message("테스트 센터 등록 성공")
                .data(testCenterService.createTestCenter())
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/manage/edit")  // 테스트 센터 수정
    public JsonBody manageEditPost() {
        return JsonBody.builder()
                .message("테스트 센터 수정 성공")
                .data(testCenterService.editTestCenter())
                .build();
    }

}
