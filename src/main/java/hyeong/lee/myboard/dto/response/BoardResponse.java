package hyeong.lee.myboard.dto.response;

import hyeong.lee.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponse {

    private final Long id;
    private final String title;
    private final String editor;
    private final String content;
    private final LocalDateTime createdAt;
    private final Integer replyCount;
    private final boolean register; // 익명 사용자 유무
    private final boolean secret; // 비밀글 유무
    
    public static BoardResponse from(Board board) {
        String editorNickname = board.getUserAccount() == null ? board.getEditor() : board.getUserAccount().getNickname();
        boolean isRegisteredUser = board.getUserAccount() != null;
        
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .editor(editorNickname)
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .replyCount(board.getReplies().size())
                .register(isRegisteredUser)
                .secret(board.getPassword() != null)
                .build();
    }
}
