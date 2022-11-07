package hyeong.lee.myboard.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventListener {

    @Async
    @EventListener
    public void handleUserRegisterEvent(UserRegisterEvent event) {
        log.info("Handling UserRegisterEvent - {}", event.getUserAccount().getUserId());
    }
}
