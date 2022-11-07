package hyeong.lee.myboard.event;

import hyeong.lee.myboard.domain.UserAccount;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private final UserAccount userAccount;

    public UserRegisterEvent(Object source, UserAccount userAccount) {
        super(source);
        this.userAccount = userAccount;
    }
}
