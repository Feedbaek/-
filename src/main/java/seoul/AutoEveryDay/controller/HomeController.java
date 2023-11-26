package seoul.AutoEveryDay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j(topic = "HomeController")
@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    @ResponseBody
    public String home() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>홈 화면</title></head>" +
                "<body>안녕하세요. 홈 화면입니다.</body>" +
                "</html>";
    }
}
