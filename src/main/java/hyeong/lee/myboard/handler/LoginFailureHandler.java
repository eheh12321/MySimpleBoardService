package hyeong.lee.myboard.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if(exception != null) {
            final FlashMap flashMap = new FlashMap();
            flashMap.put("error", "아이디 혹은 비밀번호를 확인해주세요");

            final FlashMapManager flashMapManager = new SessionFlashMapManager();
            flashMapManager.saveOutputFlashMap(flashMap, request, response);
        }
        response.sendRedirect("/login");
    }
}
