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

    public static ReplyResponseDto from(Reply reply) {
        String replyEditorNickname = reply.getUserAccount() == null ? reply.getEditor() : reply.getUserAccount().getNickname();
        UserAccountResponseDto userAccountResponseDto =
                reply.getUserAccount() == null ? null : UserAccountResponseDto.from(reply.getUserAccount());

        return ReplyResponseDto.builder()
                .id(reply.getId())
                .editor(replyEditorNickname)
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .replyUserAccount(userAccountResponseDto)
                .build();
    }
}
