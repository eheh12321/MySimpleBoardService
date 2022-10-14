package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.ReplyRequestDto;
import hyeong.lee.myboard.dto.UserAccountDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<Long> create(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @RequestBody ReplyRequestDto dto) {

        if(boardPrincipal != null) {
            UserAccountDto userAccountDto = boardPrincipal.toDto();
            dto.setEditor(userAccountDto.getNickname());
            dto.setUserAccountDto(userAccountDto);
        }
        Long replyId = replyService.create(dto);
        return ResponseEntity.ok(replyId);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Long> delete(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @PathVariable Long replyId) {

        // 비로그인 상태라도 댓글 삭제 가능 (단, 회원이 작성한 글에 대한 삭제는 불가능)
        UserAccount userAccount = boardPrincipal == null ? null : boardPrincipal.toEntity();
        replyService.delete(replyId, userAccount);
        return ResponseEntity.ok(replyId);
    }
}
