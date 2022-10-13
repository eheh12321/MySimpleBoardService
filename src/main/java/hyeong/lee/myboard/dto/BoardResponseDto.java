package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String editor;
    private final String content;
    private final LocalDateTime createdAt;
    private final Integer replyCount;

    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .editor(board.getEditor())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .replyCount(board.getReplies().size()).build();
    }
}
