package hyeong.lee.myboard.dto.request;

import hyeong.lee.myboard.domain.UserAccount;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {

    @Size(max = 30)
    @NotBlank
    private String userId;

    @Size(max=30)
    @NotBlank
    private String userPassword;

    @Size(max = 15)
    @NotBlank
    private String nickname;

    @NotBlank
    private String email;

    @Size(max = 15)
    @NotBlank
    private String phoneNumber;

    public UserAccount toEntity(PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userId(userId)
                .userPassword(passwordEncoder.encode(userPassword))
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber).build();
    }
}
