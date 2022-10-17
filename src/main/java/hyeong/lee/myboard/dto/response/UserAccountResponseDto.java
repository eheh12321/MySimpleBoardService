package hyeong.lee.myboard.dto.response;

import hyeong.lee.myboard.domain.UserAccount;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAccountResponseDto {

    private final String userId;
    private final String email;
    private final String nickname;
    private final String memo;

    public static UserAccountResponseDto from(UserAccount userAccount) {
        return UserAccountResponseDto.builder()
                .userId(userAccount.getUserId())
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .memo(userAccount.getMemo()).build();
    }
}
