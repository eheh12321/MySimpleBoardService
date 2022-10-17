package hyeong.lee.myboard.dto.request;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

    @Size(max = 50)
    @NotBlank
    private String title;

    @Size(max = 15)
    @NotBlank
    private String editor;

    @NotBlank
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
