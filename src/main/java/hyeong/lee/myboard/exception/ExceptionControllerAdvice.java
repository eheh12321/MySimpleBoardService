package hyeong.lee.myboard.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

    /**
     * BindException 처리
     * 입력 폼 데이터 검증에 실패한 경우에 대한 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResult handleBindException(BindException exception, HttpServletRequest request) {
        log.error("[BindException] handle -> {}", exception.getObjectName());

        // 필드 에러 정보 모음
        List<ErrorDetail> fieldErrorList = exception.getFieldErrors().stream()
                .map(error -> ErrorDetail.from(error, messageSource, request.getLocale()))
                .collect(Collectors.toList());

        // 객체 반환
        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(fieldErrorList)
                .message(messageSource.getMessage("BindException", null, request.getLocale()))
                .path(request.getRequestURI()).build();
    }

    
    /**
     * AccessDeniedException 처리
     * 접근 권한이 없는 경우 FORBIDDEN(403) 코드와 함께 반환
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResult handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        log.error("[AccessDeniedException] handle -> {}", exception.getMessage());

        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .errors(List.of())
                .message(messageSource.getMessage(exception.getMessage(), null, request.getLocale()))
                .path(request.getRequestURI()).build();
    }


    /**
     * 런타임 예외에 대한 처리
     * EntityNotFoundException, IllegalArgumentException 등등 나머지 전부 처리
     * (예외 종류가 많다보니 국제화 적용 X)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResult handleRuntimeException(RuntimeException exception, HttpServletRequest request) {
        log.error("[RuntimeException] handle -> {}", exception.getMessage());

        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(List.of())
                .message(exception.getMessage())
                .path(request.getRequestURI()).build();
    }

    /**
     * 기타 처리되지 못한 모든 Exception에 대한 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ErrorResult handleException(Exception exception, HttpServletRequest request) {
        log.error("[Exception] handle -> {}", exception.getMessage());

        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(List.of())
                .message(messageSource.getMessage("Exception", null, request.getLocale()))
                .path(request.getRequestURI()).build();
    }
}
