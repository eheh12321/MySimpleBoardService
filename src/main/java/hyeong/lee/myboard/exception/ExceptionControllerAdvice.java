package hyeong.lee.myboard.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    // BindException => BindingResult 인터페이스의 구현체 (내부에 bindingResult 갖고있어서 꺼내쓰면 됨)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResult handleBindException(BindException bindException, HttpServletRequest request) {
        log.error("[BindException] handle -> {}", bindException.getObjectName());

        // 필드 에러 정보 모음
        List<ErrorDetail> fieldErrorList = bindException.getFieldErrors().stream()
                .map(error -> ErrorDetail.from(error, messageSource, request.getLocale()))
                .collect(Collectors.toList());

        // 객체 반환
        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(fieldErrorList)
                .message(messageSource.getMessage("bindException", null, request.getLocale()))
                .path(request.getRequestURI()).build();
    }
}
