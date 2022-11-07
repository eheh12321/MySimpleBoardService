package hyeong.lee.myboard.config;

import hyeong.lee.myboard.dto.properties.EmailProperties;
import hyeong.lee.myboard.helper.MockEmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
@Configuration
public class EmailConfig {

    private final EmailProperties emailProperties;

    /**
     * 실제 메일 전송 안하는 MockMailSender() 기본 사용
     * -> 실제 메일 전송하려면 JavaMailSenderImpl() 사용하면 됨
     */
    @Bean
    public JavaMailSender mailSender() {
        return new MockEmailSender(); // 실제 메일 전송 X. 5초간 대기

        // 실제 메일 전송
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost(emailProperties.getHost());
//        javaMailSender.setUsername(emailProperties.getUsername());
//        javaMailSender.setPassword(emailProperties.getPassword());
//        javaMailSender.setPort(emailProperties.getPort());
//
//        Map<String, String> propMap = emailProperties.getProperties();
//        Properties javaMailProperties = new Properties();
//        for (String key : propMap.keySet()) {
//            javaMailProperties.put(key, propMap.get(key));
//        }
//        javaMailSender.setJavaMailProperties(javaMailProperties);
//
//        return javaMailSender;
    }
}
