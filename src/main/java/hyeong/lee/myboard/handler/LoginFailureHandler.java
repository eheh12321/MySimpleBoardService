package hyeong.lee.myboard.handler;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final UserAccountRepository userAccountRepository;

    @Transactional // UserAccount 테이블 update가 필요하므로 필요
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        if(exception != null) {
            final FlashMap flashMap = new FlashMap();
            if(exception instanceof BadCredentialsException) { // 아이디 혹은 비밀번호 검증 실패시
                UserAccount userAccount = userAccountRepository.findById(request.getParameter("username")).orElse(null);
                if(userAccount == null) {
                    flashMap.put("error", "존재하지 않는 회원입니다");
                } else {
                    int failCnt = userAccount.loginFailure();
                    if(failCnt < 5) {
                        flashMap.put("error", "비밀번호를 확인해주세요 (틀린 횟수: " + failCnt + "회)");
                    } else {
                        flashMap.put("error", "로그인 시도 횟수 초과로 계정이 잠깁니다.");
                    }
                }
            } else if(exception instanceof LockedException) { // 계정이 잠겼을 시
                flashMap.put("error", "로그인 시도 횟수 초과로 계정이 잠겼습니다.");
            } else {
                flashMap.put("error", "알 수 없는 에러가 발생했습니다");
            }
            final FlashMapManager flashMapManager = new SessionFlashMapManager();
            flashMapManager.saveOutputFlashMap(flashMap, request, response);
        }
        response.sendRedirect("/login");
    }
}
