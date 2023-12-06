package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.enums.RoleEnum;
import seoul.AutoEveryDay.service.AuthorizationService;
import seoul.AutoEveryDay.service.LoginService;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AuthorizationController {
    private final LoginService loginService;
    private final AuthorizationService authorizationService;

    @GetMapping("")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "userManage";
    }

    @ResponseBody
    @PostMapping("/user/role")
    public JsonBody userAdd(@RequestBody RoleEnum role) {
        User user = loginService.findByName(LoginService.getAuthenticatedUsername());
        return JsonBody.builder()
                .message("역할 변경 성공")
//                .data(authorizationService.grantRole(user, ))
                .build();
    }

    @ResponseBody
    @PostMapping("/role")
    public JsonBody roleAdd() {
        return JsonBody.builder()
                .message("역할 추가 성공")
                .data(authorizationService.createRole())
                .build();
    }

    @ResponseBody
    @DeleteMapping("/role")
    public JsonBody roleDelete() {
        // 역할에 해당하는 사용자가 없는지 확인 필요. 없는 경우만 삭제 가능
        return JsonBody.builder()
                .message("역할 삭제 성공")
                .data(authorizationService.deleteRole())
                .build();
    }

    @ResponseBody
    @PostMapping("/role/privilige")
    public JsonBody priviligeAdd() {
        return JsonBody.builder()
                .message("역할에게 권한 부여 성공")
                .data(authorizationService.grantPrivilege())
                .build();
    }

    @ResponseBody
    @DeleteMapping("/role/privilige")
    public JsonBody priviligeDelete() {
        return JsonBody.builder()
                .message("역할에서 권한 회수 성공")
                .data(authorizationService.revokePrivilege())
                .build();
    }

    @PostMapping("/privilege")
    public JsonBody privilegeAdd() {
        return JsonBody.builder()
                .message("권한 추가 성공")
                .data(authorizationService.createPrivilege())
                .build();
    }

    @DeleteMapping("/privilege")
    public JsonBody privilegeDelete() {
        return JsonBody.builder()
                .message("권한 삭제 성공")
                .data(authorizationService.deletePrivilege())
                .build();
    }
}
