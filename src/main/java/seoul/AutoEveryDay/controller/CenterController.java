package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.dto.TrackDto;
import seoul.AutoEveryDay.dto.ReserveTrackDto;
import seoul.AutoEveryDay.entity.Track;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.LoginService;
import seoul.AutoEveryDay.service.TrackService;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/center")
public class CenterController { // 테스트 트랙 관련 컨트롤러
    private final TrackService trackService;
    private final LoginService userService;

    // 예약 관련 컨트롤러
    @GetMapping("/track/reserve")  // 테스트 트랙 예약 페이지
    public String reserveGet(Model model) {
        model.addAttribute("trackList", trackService.getAllTestTrack());
        return "trackSelect";
    }

    @GetMapping("/track/reserve/{trackId}")  // 테스트 트랙 예약 날짜 선택 페이지
    public String reserveGet(@PathVariable("trackId") Long trackId, Model model) {
        Track track = trackService.getTestTrack(trackId);
        List<String> unavailableDateList = trackService.getUnavailableDateList(track);
        model.addAttribute("dateArr", unavailableDateList);
        model.addAttribute("trackId", trackId);
        return "trackReserveDate";
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TRACK_RESERVE')")
    @PostMapping("/track/reserve")  // 테스트 트랙 예약
    public JsonBody reservePost(@Validated @RequestBody ReserveTrackDto reserveTrackDto) {
        User user = userService.getLoginUser();
        return JsonBody.builder()
                .message("예약 성공")
                .data(trackService.createReserveHistory(reserveTrackDto, user))
                .build();
    }
    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TRACK_RESERVE')")
    @DeleteMapping("/track/reserve")  // 테스트 트랙 예약 취소
    public JsonBody reserveDelete(@Validated @RequestParam ReserveTrackDto reserveTrackDto) {
        User user = userService.getLoginUser();
        return JsonBody.builder()
                .message("예약 취소 성공")
                .data(trackService.deleteReserveHistory(reserveTrackDto, user))
                .build();
    }

    // 관리자 관련 컨트롤러
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/track/manage")  // 테스트 트랙 관리 페이지
    public String allTrackGet(Model model) {
        model.addAttribute("testTrackList", trackService.getAllTestTrack());
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
    @PostMapping("/track/manage")  // 테스트 트랙 등록
    public JsonBody managePost(@Validated TrackDto trackDto) {
        return JsonBody.builder()
                .message("테스트 트랙 등록 성공")
                .data(trackService.createTestTrack(trackDto))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping("/track/manage")  // 테스트 트랙 수정
    public JsonBody manageEdit(@Validated TrackDto trackDto) {
        return JsonBody.builder()
                .message("테스트 트랙 수정 성공")
                .data(trackService.editTestTrack(trackDto))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping("/track/manage")  // 테스트 트랙 삭제
    public JsonBody manageDelete(@RequestParam("id") Long trackId) {
        return JsonBody.builder()
                .message("테스트 트랙 삭제 성공")
                .data(trackService.deleteTestTrack(trackId))
                .build();
    }
}
