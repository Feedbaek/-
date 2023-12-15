package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.dto.RoleChangeReq;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.AuthorizeService;
import seoul.AutoEveryDay.service.LoginService;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping("/authorize")
public class AuthorizeController {
    private final LoginService loginService;
    private final AuthorizeService authorizeService;

    @GetMapping("/user")
    public String user() {
        return "authorizeManage";
    }

    @ResponseBody
    @PostMapping("/user/role")
    public JsonBody userAdd(@Validated RoleChangeReq req) {
        User user = loginService.findById(req.getUserId());
        return JsonBody.builder()
                .message("역할 변경 성공")
                .data(authorizeService.grantRole(user, req.getRole()))
                .build();
    }

    @ResponseBody
    @PostMapping("/role")
    public JsonBody roleAdd(@RequestParam String roleName) {
        return JsonBody.builder()
                .message("역할 추가 성공")
                .data(authorizeService.createRole(roleName))
                .build();
    }

    @ResponseBody
    @DeleteMapping("/role")
    public JsonBody roleDelete(@RequestParam String roleName) {
        // 역할에 해당하는 사용자가 없는지 확인 필요. 없는 경우만 삭제 가능
        return JsonBody.builder()
                .message("역할 삭제 성공")
                .data(authorizeService.deleteRole(roleName))
                .build();
    }

    @ResponseBody
    @PostMapping("/role/privilige")
    public JsonBody priviligeAdd(@RequestParam String roleName, @RequestParam String privilegeName) {
        return JsonBody.builder()
                .message("역할에게 권한 부여 성공")
                .data(authorizeService.grantPrivilege(roleName, privilegeName))
                .build();
    }

    @ResponseBody
    @DeleteMapping("/role/privilige")
    public JsonBody priviligeDelete(@RequestParam String roleName, @RequestParam String privilegeName) {
        return JsonBody.builder()
                .message("역할에서 권한 회수 성공")
                .data(authorizeService.revokePrivilege(roleName, privilegeName))
                .build();
    }

    @PostMapping("/privilege")
    public JsonBody privilegeAdd(@RequestParam String privilegeName) {
        return JsonBody.builder()
                .message("권한 추가 성공")
                .data(authorizeService.createPrivilege(privilegeName))
                .build();
    }

    @DeleteMapping("/privilege")
    public JsonBody privilegeDelete(@RequestParam String privilegeName) {
        return JsonBody.builder()
                .message("권한 삭제 성공")
                .data(authorizeService.deletePrivilege(privilegeName))
                .build();
    }
}
