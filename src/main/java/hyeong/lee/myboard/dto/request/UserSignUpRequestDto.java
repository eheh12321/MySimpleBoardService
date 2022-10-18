package hyeong.lee.myboard.dto.request;

import hyeong.lee.myboard.domain.UserAccount;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {

    /**
     * 아이디 검증
     * 시작은 영문자 또는 숫자로만 시작
     * 중간에는 영문자/숫자/특수기호(-_.)이 올 수 있음
     *   - 특수기호는 연달아서 올 수 없음 (?!)
     *   - 3글자 이상으로 구성
     * 마지막은 영문자 또는 숫자로만 끝남
     * 총 5글자 이상
     */
    @Size(min = 5, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,}[a-zA-Z0-9]$", message = "아이디 규격에 맞지 않습니다.")
    @NotBlank
    private String userId;

    /**
     * 비밀번호 검증
     * 영어 소문자, 대문자, 숫자, 특수기호가 모두 포함되어야 함
     */
    @Size(max=30)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_+-=`<>?]).{6,}", message = "비밀번호 규격에 맞지 않습니다.")
    @NotBlank
    private String userPassword;

    @Size(max = 15)
    @NotBlank
    private String nickname;

    /**
     * 이메일 검증
     * 시작은 영문자 또는 숫자로만 시작하고 1글자 이상 존재함
     * "@" 그리고 "\\." 은 반드시 존재함
     * "\\." 뒤에는 영문자 또는 "."이 올 수 있음 (2~5글자)
     *   - .co.kr 과 같이 .이 여러개 올 수 있기 때문
     */
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z.]{2,5}$", message = "이메일 규격에 맞지 않습니다.")
    @NotBlank
    private String email;

    /**
     * 전화번호 검증: 000-000(0)-0000 형식 고정
     */
    @Size(max = 15)
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}", message = "전화번호 규격(000-000(0)-0000)에 맞지 않습니다")
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
