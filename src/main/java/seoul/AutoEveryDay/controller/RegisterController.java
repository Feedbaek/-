package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import seoul.AutoEveryDay.dto.RegisterReq;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
@Slf4j(topic = "registerController")
public class RegisterController {
    private final UserService userService;

    @GetMapping("")
    public String register() {
        return "register";
    }
    @PostMapping("")
    public String register(RegisterReq registerReq) {
        User user = User.builder()
                .username(registerReq.getUsername())
                .password(registerReq.getPassword())
                .build();
        userService.save(user);
        return "login";
    }
}
