package hyeong.lee.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan("hyeong.lee.myboard.dto.properties")
@SpringBootApplication
public class MyBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBoardApplication.class, args);
    }

}
