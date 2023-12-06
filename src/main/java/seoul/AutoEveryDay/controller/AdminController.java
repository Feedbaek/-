package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "userManage";
    }

    @ResponseBody
    @PostMapping("/user/privilege")
    public JsonBody userPost() {
        return JsonBody.builder()
                .message("권한 부여 성공")
                .build();
    }

    @ResponseBody
    @DeleteMapping("/user/privilege")
    public JsonBody userDelete() {
        return JsonBody.builder()
                .message("권한 삭제 성공")
                .build();
    }
}
