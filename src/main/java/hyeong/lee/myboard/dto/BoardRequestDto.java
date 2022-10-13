package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String editor;
    private String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .editor(editor)
                .content(content).build();
    }
}
