package hyeong.lee.myboard.dto.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.mail")
@ConstructorBinding
public class EmailProperties {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final Map<String, String> properties;
}
