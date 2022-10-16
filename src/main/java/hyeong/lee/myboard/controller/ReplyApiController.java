package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.ReplyRequestDto;
import hyeong.lee.myboard.dto.UserAccountDto;
import hyeong.lee.myboard.dto.exception.ErrorDetail;
import hyeong.lee.myboard.dto.exception.ErrorResult;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.ReplyService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal @Nullable BoardPrincipal boardPrincipal,
            @Valid @RequestBody ReplyRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            List<ErrorDetail> fieldErrorList = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorList.add(ErrorDetail.from(fieldError));
            }
            ErrorResult errorResult = ErrorResult.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .errors(fieldErrorList)
                    .message("입력값 검증에 실패했습니다")
                    .path(request.getRequestURI()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }

        if (boardPrincipal != null) {
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
