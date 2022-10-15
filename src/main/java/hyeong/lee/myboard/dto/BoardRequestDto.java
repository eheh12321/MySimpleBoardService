package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String editor;
    private String content;
    private MultipartFile[] files;
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
