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
    private String email;
    private String nickname;
    private String memo;

    public static UserAccountDto from(UserAccount entity) {
        return UserAccountDto.builder()
                .userId(entity.getUserId())
                .password(entity.getUserPassword())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .memo(entity.getMemo()).build();
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .userId(userId)
                .userPassword(password)
                .email(email)
                .nickname(nickname)
                .memo(memo).build();
    }
}
