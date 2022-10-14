package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String editor;
    private String content;
    private UserAccountDto userAccountDto;

    public Board toEntity() {
        UserAccount userAccount = userAccountDto == null ? null : userAccountDto.toEntity();

        return Board.builder()
                .title(title)
                .editor(editor)
                .content(content)
                .userAccount(userAccount)
                .build();
    }
}
