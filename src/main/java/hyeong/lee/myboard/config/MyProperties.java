package hyeong.lee.myboard.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "my")
@ConstructorBinding
public class MyProperties {

    @Getter private final String resourcesPath;
}
