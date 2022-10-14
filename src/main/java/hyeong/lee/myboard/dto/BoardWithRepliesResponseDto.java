package hyeong.lee.myboard.dto;

import hyeong.lee.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class BoardWithRepliesResponseDto {

    private final Long id;
    private final String title;
    private final String editor;
    private final String content;
    private final LocalDateTime createdAt;
    private final List<ReplyResponseDto> replies;
    private final UserAccountResponseDto userAccount;

    public static BoardWithRepliesResponseDto from(Board board) {
        UserAccountResponseDto userAccountResponseDto =
                board.getUserAccount() == null ? null : UserAccountResponseDto.from(board.getUserAccount());

        return BoardWithRepliesResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .editor(board.getEditor())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .replies(board.getReplies().stream().map(ReplyResponseDto::from).collect(Collectors.toList()))
                .userAccount(userAccountResponseDto)
                .build();
    }
}
