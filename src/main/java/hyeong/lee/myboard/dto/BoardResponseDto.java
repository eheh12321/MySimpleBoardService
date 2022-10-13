package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String editor;
    private final String content;
    private final LocalDateTime createdAt;

    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getEditor(),
                board.getContent(),
                board.getCreatedAt());
    }
}
