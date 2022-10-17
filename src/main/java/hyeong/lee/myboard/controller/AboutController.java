package hyeong.lee.myboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("/about")
@Controller
public class AboutController {

    @GetMapping
    public String about() {
        return "aboutPage";
    }

    @GetMapping("/error-test-4xx")
    public void make4xxError(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "임의로 4xx 에러 생성");
    }

    @GetMapping("/error-test-404")
    public void make404Error(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "임의로 404 에러 생성");
    }

    @GetMapping("/error-test-5xx")
    public void make5xxError(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT, "임의로 5xx 에러 생성");
    }

    @GetMapping("/error-test-500")
    public void make500Error(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "임의로 500 에러 생성");
    }
}
