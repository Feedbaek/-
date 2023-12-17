package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seoul.AutoEveryDay.dto.JsonBody;
import seoul.AutoEveryDay.dto.RoleChangeReq;
import seoul.AutoEveryDay.dto.UserDto;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.AuthorizeService;
import seoul.AutoEveryDay.service.LoginService;
import seoul.AutoEveryDay.utils.Converter;

import java.util.*;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping("/authorize")
public class AuthorizeController {
    private final LoginService loginService;
    private final AuthorizeService authorizeService;
    private final Converter converter;

    @GetMapping("/manage")
    public String manage(Model model) {
        List<UserDto> userList = loginService.getAllUserDto();
        model.addAttribute("userList", userList);
        return "authorizeManage";
    }

    @ResponseBody
    @GetMapping("/user/role")
    public JsonBody userRole(@RequestParam("id") Long userId) {
        List<Object> roleLists = new ArrayList<>();
        List<Role> assignedRole = loginService.findUserRoles(userId);
        List<Role> unassignedRole = new ArrayList<>();

        // 전제 role 목록 중에서 assignedRole에 포함된 role 목록을 제외한 목록을 unassignedRole에 추가
        List<Role> allRoles = loginService.findAllRoles();
        for (Role role : allRoles) {
            if (!assignedRole.contains(role)) {
                unassignedRole.add(role);
            }
        }

        roleLists.add(converter.convertToRoleDtoList(assignedRole));
        roleLists.add(converter.convertToRoleDtoList(unassignedRole));
        return JsonBody.builder()
                .message("역할 조회 성공")
                .data(roleLists)
                .build();
    }

    @ResponseBody
    @PostMapping("/user/role")
    public JsonBody userAdd(@Validated @RequestBody RoleChangeReq req) {
        User user = loginService.findById(req.getUserId());
        return JsonBody.builder()
                .message("역할 부여 성공")
                .data(authorizeService.grantRole(user, req.getRoleName()))
                .build();
    }

    @ResponseBody
    @DeleteMapping("/user/role")
    public JsonBody userDelete(@Validated @RequestBody RoleChangeReq req) {
        User user = loginService.findById(req.getUserId());
        return JsonBody.builder()
                .message("역할 회수 성공")
                .data(authorizeService.revokeRole(user, req.getRoleName()))
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
