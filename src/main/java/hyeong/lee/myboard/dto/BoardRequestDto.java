package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import lombok.Data;

@Data
public class BoardRequestDto {

    private final String title;
    private final String editor;
    private final String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .editor(editor)
                .content(content).build();
    }
}
