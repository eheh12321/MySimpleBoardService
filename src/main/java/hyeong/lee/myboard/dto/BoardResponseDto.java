package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String editor;
    private final String content;
    private final LocalDateTime createdAt;
    private final Integer replyCount;
    private final boolean isRegisteredUser; // 익명 사용자 유무
    
    public static BoardResponseDto from(Board board) {
        String editorNickname = board.getUserAccount() == null ? board.getEditor() : board.getUserAccount().getNickname();
        boolean isRegisteredUser = board.getUserAccount() != null;
        
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .editor(editorNickname)
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .replyCount(board.getReplies().size())
                .isRegisteredUser(isRegisteredUser)
                .build();
    }
}
