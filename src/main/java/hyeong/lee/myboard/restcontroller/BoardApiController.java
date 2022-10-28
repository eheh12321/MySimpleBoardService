package hyeong.lee.myboard.restcontroller;

import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @Valid BoardRequest.BoardPostDto boardPostDto) {
        UserAccountDto userAccountDto = (boardPrincipal == null ? null : boardPrincipal.toDto());
        Long boardId = boardService.create(boardPostDto, userAccountDto);
        return ResponseEntity.ok(boardId);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<?> update(@PathVariable Long boardId,
                                       @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
                                       @Valid @RequestBody BoardRequest.BoardPatchDto boardPatchDto) {
        UserAccountDto userAccountDto = (boardPrincipal == null ? null : boardPrincipal.toDto());
        boardService.update(boardId, boardPatchDto, userAccountDto);
        return ResponseEntity.ok(boardId);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Long> delete(@PathVariable Long boardId,
                                       @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal) {
        UserAccountDto userAccountDto = (boardPrincipal == null ? null : boardPrincipal.toDto());
        boardService.delete(boardId, userAccountDto);
        return ResponseEntity.ok(boardId);
    }
}
