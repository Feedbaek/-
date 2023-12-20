package seoul.AutoEveryDay.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.*;
import seoul.AutoEveryDay.dto.track.ReserveTrackDto;
import seoul.AutoEveryDay.dto.track.TrackReq;
import seoul.AutoEveryDay.entity.Track;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.DriveService;
import seoul.AutoEveryDay.service.LoginService;
import seoul.AutoEveryDay.service.TrackService;

import java.util.List;

@Controller
@PreAuthorize(value = "hasAuthority('READ')")
@RequiredArgsConstructor
@RequestMapping("/center")
public class CenterController { // 테스트 트랙 관련 컨트롤러
    private final TrackService trackService;
    private final LoginService userService;
    private final DriveService driveService;

    // 예약 관련 컨트롤러
    @GetMapping("/track/reserve")  // 테스트 트랙 예약 페이지
    public String reserveGet(Model model) {
        model.addAttribute("trackList", trackService.getAllTestTrack());
        return "track/trackSelect";
    }

    @PreAuthorize(value = "hasAuthority('TRACK_RESERVE')")
    @GetMapping("/track/reserve/{trackId}")  // 테스트 트랙 예약 날짜 선택 페이지
    public String reserveGet(@PathVariable("trackId") Long trackId, Model model) {
        Track track = trackService.getTestTrack(trackId);
        List<String> unavailableDateList = trackService.getUnavailableDateList(track);
        model.addAttribute("dateArr", unavailableDateList);
        model.addAttribute("trackId", trackId);
        return "track/trackReserveDate";
    }

    @PreAuthorize(value = "hasAuthority('TRACK_RESERVE')")
    @GetMapping("/track/reserve/history")  // 테스트 트랙 예약 날짜 선택 페이지
    public String reserveHistoryGet(Model model) {
        User user = userService.getLoginUser();
        String[] reserveHistoryTitles = {"트랙 이름", "예약 날짜", "상태", "취소"};
        List<List<String>> reserveHistoryList = trackService.getReserveHistory(user);
        model.addAttribute("reserveHistoryTitles", reserveHistoryTitles);
        model.addAttribute("reserveHistoryList", reserveHistoryList);
        return "track/trackReserveHistory";
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
    @PostMapping("/track/reserve/cancel/{historyId}")  // 테스트 트랙 예약 취소
    public JsonBody reserveCancelPost(@PathVariable("historyId") Long historyId) {
        return JsonBody.builder()
                .message("예약 취소 성공")
                .data(trackService.cancelReserveHistory(historyId))
                .build();
    }


    @ResponseBody
    @PreAuthorize(value = "hasAuthority('TRACK_RESERVE')")
    @DeleteMapping("/track/reserve/{historyId}")  // 테스트 트랙 예약 삭제
    public JsonBody reserveDelete(@PathVariable("historyId") Long historyId) {
        return JsonBody.builder()
                .message("예약 취소 성공")
                .data(trackService.deleteReserveHistory(historyId))
                .build();
    }

    // 관리자 관련 컨트롤러
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/track/manage")  // 테스트 트랙 관리 페이지
    public String allTrackGet(Model model) {
        model.addAttribute("testTrackList", trackService.getAllTestTrack());
        return "track/trackManage";
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/track/manage")  // 테스트 트랙 등록
    public JsonBody managePost(@Validated TrackReq trackReq) {
        return JsonBody.builder()
                .message("테스트 트랙 등록 성공")
                .data(trackService.createTestTrack(trackReq))
                .build();
    }

    @ResponseBody
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping("/track/manage")  // 테스트 트랙 수정
    public JsonBody manageEdit(@Validated TrackReq trackReq) {
        return JsonBody.builder()
                .message("테스트 트랙 수정 성공")
                .data(trackService.editTestTrack(trackReq))
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

    // 주행 기록 관련 컨트롤러
    @GetMapping("/drive/history")  // 주행 기록 페이지
    public String driveHistoryGet(Model model) {
        String[] driveHistoryTitles = {"운전자", "차량 번호", "차량 종류", "주행 날짜", "주행 거리", "주행 시간", "평균 속도", "최고 속도", "삭제"};
        List<List<String>> driveHistoryList = driveService.getDriveHistoryList();
        model.addAttribute("driveHistoryTitles", driveHistoryTitles);
        model.addAttribute("driveHistoryList", driveHistoryList);
        return "driveHistory";
    }
    @ResponseBody
    @Operation(summary = "주행 기록 등록")
    @PostMapping("/drive/history")  // 주행 기록 등록
    public JsonBody driveHistoryPost(@Validated @RequestBody DriveHistoryDto driveHistoryDto) {
        User user = userService.getLoginUser();
        driveHistoryDto.setUserId(user.getId());
        return JsonBody.builder()
                .message("주행 기록 등록 성공")
                .data(driveService.addDriveHistory(driveHistoryDto))
                .build();
    }

    @ResponseBody
    @DeleteMapping("/drive/history/{id}")  // 주행 기록 삭제
    public JsonBody driveHistoryDelete(@PathVariable("id") Long id) {
        return JsonBody.builder()
                .message("주행 기록 삭제 성공")
                .data(driveService.deleteDriveHistory(id))
                .build();
    }


}
