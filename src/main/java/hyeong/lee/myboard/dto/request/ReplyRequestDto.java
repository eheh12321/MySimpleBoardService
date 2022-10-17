package hyeong.lee.myboard.dto.request;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.domain.UserAccount;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequestDto {
        
    private Long boardId;

    @Size(max = 15)
    @NotBlank
    private String editor;
    
    @NotBlank
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
