package hyeong.lee.myboard.controller.restcontroller;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.BoardRequestDto;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
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
            @Valid BoardRequestDto dto) {

        // 회원이 작성한 경우 계정 정보 삽입
        if(boardPrincipal != null) {
            UserAccountDto userAccountDto = boardPrincipal.toDto();
            dto.setEditor(userAccountDto.getNickname());
            dto.setUserAccountDto(userAccountDto);
        }
        // 게시글 저장
        Long boardId = boardService.create(dto);

        return ResponseEntity.ok(boardId);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<?> update(@PathVariable Long boardId,
                                       @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
                                       @Valid @RequestBody BoardRequestDto dto) {

        if(boardPrincipal == null) { // 비로그인 상태라면 글 수정 불가
            throw new AccessDeniedException("AccessDeniedException.Login");
        }

        UserAccountDto userAccountDto = boardPrincipal.toDto();
        dto.setUserAccountDto(userAccountDto);

        boardService.update(boardId, dto);
        return ResponseEntity.ok(boardId);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Long> delete(@PathVariable Long boardId,
                                       @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal) {

        // 비로그인 상태라도 게시글 삭제는 가능 (단, 회원이 작성한 글에 대한 삭제는 불가능)
        UserAccount userAccount = boardPrincipal == null ? null : boardPrincipal.toEntity();
        boardService.delete(boardId, userAccount);
        return ResponseEntity.ok(boardId);
    }
}
