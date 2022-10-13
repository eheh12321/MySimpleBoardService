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

    public static ReplyResponseDto from(Reply reply) {
        return ReplyResponseDto.builder()
                .id(reply.getId())
                .editor(reply.getEditor())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt()).build();
    }
}
