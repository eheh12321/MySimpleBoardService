package hyeong.lee.myboard.event;

import hyeong.lee.myboard.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handleUserRegisterEvent(UserRegisterEvent event) {
        log.info("Handling UserRegisterEvent - {}", event.getUserAccount().getUserId());
        log.info(">> 메일 전송 준비");
        emailService.sendEmail("회원가입을 환영합니다!",
                "회원가입 하신 것을 환영합니다, " + event.getUserAccount().getNickname() + "님!",
                event.getUserAccount().getEmail());
        log.info("<< 메일 전송 완료 - To.{}", event.getUserAccount().getEmail());
    }
}
