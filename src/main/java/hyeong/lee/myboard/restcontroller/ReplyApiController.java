package hyeong.lee.myboard.restcontroller;

import hyeong.lee.myboard.dto.request.ReplyRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @Valid @RequestBody ReplyRequest replyRequest) {
        UserAccountDto userAccountDto = (boardPrincipal == null ? null : boardPrincipal.toDto());
        Long replyId = replyService.create(replyRequest, userAccountDto);
        return ResponseEntity.ok(replyId);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Long> delete(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @PathVariable Long replyId) {
        UserAccountDto userAccountDto = (boardPrincipal == null ? null : boardPrincipal.toDto());
        replyService.delete(replyId, userAccountDto);
        return ResponseEntity.ok(replyId);
    }
}
