package hyeong.lee.myboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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
    public String loginPage(@ModelAttribute("error") String loginFailureError,
                            HttpServletRequest request,
                            Model model) {

        // flashMap을 통해 loginFailureError 값이 전달 된 경우 (없을 경우 ""이 리턴되기 때문에 isEmpty() 처리)
        // ** flashMap 특성 상 리다이렉트되는 시점에 1회만 꺼낼 수 있음 (새로고침시 더이상 보이지 않음) **
        if(loginFailureError != null && !loginFailureError.isEmpty()) {
            model.addAttribute("loginFailureError", loginFailureError);
        } else {
            // 로그인 페이지에 새로 들어온 경우에만 <-- 로그인 실패로 리다이렉트된 경우 referer 값을 덮어씌워버리기 때문
            // 세션에 이전 페이지 경로 정보를 referer 헤더를 통해 가져온 뒤 저장해둔다
            String referer = request.getHeader("referer");
            request.getSession().setAttribute("url_prior_login", referer);
        }
        return "auth/login";
    }
}
