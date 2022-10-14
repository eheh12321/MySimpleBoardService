package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.BoardRequestDto;
import hyeong.lee.myboard.dto.UserAccountDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Long> create(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @RequestBody BoardRequestDto dto) {

        if(boardPrincipal != null) {
            UserAccountDto userAccountDto = boardPrincipal.toDto();
            dto.setEditor(userAccountDto.getUserId());
            dto.setUserAccountDto(userAccountDto);
        }
        Long boardId = boardService.create(dto);
        return ResponseEntity.ok(boardId);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Long> update(@PathVariable Long boardId, @RequestBody BoardRequestDto dto) {
        boardService.update(boardId, dto);
        return ResponseEntity.ok(boardId);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Long> delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.ok(boardId);
    }
}
