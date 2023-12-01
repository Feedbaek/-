package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("")
    public String admin() {
        return "admin";
    }
}
