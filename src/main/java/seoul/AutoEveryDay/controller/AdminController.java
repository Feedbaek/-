package seoul.AutoEveryDay.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.repository.PrivilegeRepository;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_PRIVILEGE')")
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("")
    public String admin() {
        return "admin";
    }
}
