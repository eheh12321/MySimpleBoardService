package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequestDto {

    private Long boardId;
    private String editor;
    private String content;

    public Reply toEntity(Board board) {
        return Reply.builder()
                .board(board)
                .editor(editor)
                .content(content).build();
    }
}
