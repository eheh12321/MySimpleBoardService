package hyeong.lee.myboard.dto.request;

import hyeong.lee.myboard.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto {

    private String userId;
    private String password;
    private String auth;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String memo;
    private int loginFailCount;

    public static UserAccountDto from(UserAccount entity) {
        return UserAccountDto.builder()
                .userId(entity.getUserId())
                .password(entity.getUserPassword())
                .auth(entity.getAuth())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .phoneNumber(entity.getPhoneNumber())
                .memo(entity.getMemo())
                .loginFailCount(entity.getLoginFailCount()).build();
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .userId(userId)
                .userPassword(password)
                .auth(auth)
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .memo(memo)
                .loginFailCount(loginFailCount).build();
    }
}
