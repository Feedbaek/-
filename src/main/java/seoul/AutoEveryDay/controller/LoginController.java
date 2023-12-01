package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static seoul.AutoEveryDay.service.LoginService.isAuthenticated;

@Slf4j(topic = "loginController")
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @GetMapping("")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }
}
