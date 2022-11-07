package hyeong.lee.myboard.config;

import hyeong.lee.myboard.dto.properties.MyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MyProperties myProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String storagePath = Paths.get(myProperties.getResourcesPath()).toUri().toString();

        // 해당 경로로 오는 요청을 처리하는 핸들러 추가
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(storagePath);
    }
}
