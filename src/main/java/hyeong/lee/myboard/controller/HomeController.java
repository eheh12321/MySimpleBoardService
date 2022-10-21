package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.security.BoardPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/")
@Controller
public class HomeController {

    @GetMapping
    public String forward() {
        return "forward:/boards";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/index";
    }

    // @PostMapping("/login")은 스프링 시큐리티가 내부적으로 처리한다
    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }
}
