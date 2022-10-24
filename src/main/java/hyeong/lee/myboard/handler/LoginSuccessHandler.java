package hyeong.lee.myboard.handler;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserAccountRepository userAccountRepository;

    @Transactional // UserAccount 테이블 update가 필요하므로 필요
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        BoardPrincipal principal = (BoardPrincipal) auth.getPrincipal();
        // 로그인에 성공했으므로 로그인 시도횟수 0으로 초기화
        userAccountRepository.findById(principal.getUsername())
                        .ifPresent(UserAccount::loginSuccess);

        HttpSession session = request.getSession();
        if(session != null) {
            String redirectUrl = (String) session.getAttribute("url_prior_login");
            if(redirectUrl != null) {
                session.removeAttribute("url_prior_login");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                return;
            }
        }
        response.sendRedirect("/");
    }
}
