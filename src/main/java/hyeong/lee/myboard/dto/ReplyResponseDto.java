package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Reply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyResponseDto {

    private final Long id;
    private final String editor;
    private final String content;
    private final LocalDateTime createdAt;
    private final UserAccountResponseDto replyUserAccount;
    private final boolean isRegisteredUser; // 익명 사용자 유무

    public static ReplyResponseDto from(Reply reply) {
        String replyEditorNickname = reply.getUserAccount() == null ? reply.getEditor() : reply.getUserAccount().getNickname();
        UserAccountResponseDto userAccountResponseDto =
                reply.getUserAccount() == null ? null : UserAccountResponseDto.from(reply.getUserAccount());
        boolean isRegisteredUser = reply.getUserAccount() != null;

        return ReplyResponseDto.builder()
                .id(reply.getId())
                .editor(replyEditorNickname)
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .replyUserAccount(userAccountResponseDto)
                .isRegisteredUser(isRegisteredUser)
                .build();
    }
}
