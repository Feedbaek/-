package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import seoul.AutoEveryDay.dto.RegisterReq;
import seoul.AutoEveryDay.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
@Slf4j(topic = "registerController")
public class RegisterController {
    private final UserService userService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("")
    public String register() {
        return "register";
    }
    @PostMapping("")
    public String register(RegisterReq registerReq) {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        userService.register(registerReq);
        return "login";
    }
}
