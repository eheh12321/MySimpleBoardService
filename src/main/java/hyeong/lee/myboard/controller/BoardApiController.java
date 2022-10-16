package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.BoardRequestDto;
import hyeong.lee.myboard.dto.UserAccountDto;
import hyeong.lee.myboard.dto.exception.ErrorDetail;
import hyeong.lee.myboard.dto.exception.ErrorResult;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardApiController {

    private final BoardService boardService;
    private final MessageSource messageSource;

    @PostMapping
    @ExceptionHandler
    public ResponseEntity<?> create(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @Valid BoardRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) { // TODO: 중복 제거하기
            List<ErrorDetail> fieldErrorList = bindingResult.getFieldErrors().stream()
                    .map(error -> ErrorDetail.from(error, messageSource, request.getLocale()))
                    .collect(Collectors.toList());

            ErrorResult errorResult = ErrorResult.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .errors(fieldErrorList)
                    .message("입력값 검증에 실패했습니다") // TODO: 하드코딩 변경
                    .path(request.getRequestURI()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }


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
                                       @Valid @RequestBody BoardRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            List<ErrorDetail> fieldErrorList = bindingResult.getFieldErrors().stream()
                    .map(error -> ErrorDetail.from(error, messageSource, request.getLocale()))
                    .collect(Collectors.toList());

            ErrorResult errorResult = ErrorResult.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .errors(fieldErrorList)
                    .message("입력값 검증에 실패했습니다")
                    .path(request.getRequestURI()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
        
        if(boardPrincipal == null) { // 비로그인 상태라면 글 수정 불가
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(boardId);
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
