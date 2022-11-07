package hyeong.lee.myboard.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishCustomEvent(ApplicationEvent event) {
        log.info("Publishing Custom Event - {}", event.getSource());
        publisher.publishEvent(event);
    }
}
