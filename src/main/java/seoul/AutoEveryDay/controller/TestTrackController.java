package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.dto.TestTrackDto;
import seoul.AutoEveryDay.dto.TestHistoryDto;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.LoginService;
import seoul.AutoEveryDay.service.TestTrackService;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('TEST_TRACK')")
@RequestMapping("/track")
public class TestTrackController { // 테스트 트랙 관련 컨트롤러
    private final TestTrackService testTrackService;
    private final LoginService userService;

    // 예약 관련 컨트롤러
    @GetMapping("/reserve")  // 테스트 트랙 예약 페이지
    public String reserveGet(Model model) {
        model.addAttribute("testTrackList", testTrackService.getAllTestTrack());
        model.addAttribute("minDay", LocalDate.now());
        model.addAttribute("maxDay", LocalDate.now().plusDays(7));
        return "trackReserve";
    }

    @ResponseBody
    @PostMapping("/reserve")  // 테스트 트랙 예약
    public JsonBody reservePost(TestHistoryDto testHistoryDto) {
        User user = userService.getLoginUser();
        return JsonBody.builder()
                .message("예약 성공")
                .data(testTrackService.createTestHistory(testHistoryDto, user))
                .build();
    }
    @ResponseBody
    @DeleteMapping("/reserve")  // 테스트 트랙 예약 취소
    public JsonBody reserveDelete(@RequestParam TestHistoryDto testHistoryDto) {
        User user = userService.getLoginUser();
        return JsonBody.builder()
                .message("예약 취소 성공")
                .data(testTrackService.deleteTestHistory(testHistoryDto, user))
                .build();
    }

    // 관리자 관련 컨트롤러
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/manage")  // 테스트 트랙 관리 페이지
    public String allTrackGet(Model model) {
        model.addAttribute("testTrackList", testTrackService.getAllTestTrack());
        return "trackManage";
    }

//    @ResponseBody
//    @PreAuthorize(value = "hasAuthority('ADMIN')")
//    @GetMapping("/manage")  // 테스트 트랙 상세 조회
//    public JsonBody trackGet(@RequestParam String name) {
//        return JsonBody.builder()
//                .message("테스트 트랙 조회 성공")
//                .data(testTrackService.getTestTrack(name))
//                .build();
//    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/manage")  // 테스트 트랙 등록
    public JsonBody managePost(TestTrackDto testTrackDto) {
        return JsonBody.builder()
                .message("테스트 트랙 등록 성공")
                .data(testTrackService.createTestTrack(testTrackDto))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping("/manage")  // 테스트 트랙 수정
    public JsonBody manageEdit(TestTrackDto testTrackDto) {
        return JsonBody.builder()
                .message("테스트 트랙 수정 성공")
                .data(testTrackService.editTestTrack(testTrackDto))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping("/manage")  // 테스트 트랙 삭제
    public JsonBody manageDelete(@RequestParam String name) {
        return JsonBody.builder()
                .message("테스트 트랙 삭제 성공")
                .data(testTrackService.deleteTestTrack(name))
                .build();
    }
}
