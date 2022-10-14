package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.domain.UserAccount;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequestDto {
    private Long boardId;
    private String editor;
    private String content;
    private UserAccountDto userAccountDto;

    public Reply toEntity(Board board) {
        UserAccount userAccount = userAccountDto == null ? null : userAccountDto.toEntity();

        return Reply.builder()
                .board(board)
                .editor(editor)
                .content(content)
                .userAccount(userAccount)
                .build();
    }
}
