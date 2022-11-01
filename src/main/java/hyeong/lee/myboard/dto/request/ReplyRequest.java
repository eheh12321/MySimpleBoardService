package hyeong.lee.myboard.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyRequest {
        
    private Long boardId;

    @Size(max = 15)
    @NotBlank
    private String editor;
    
    @NotBlank
    private String content;
}
