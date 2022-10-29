package hyeong.lee.myboard.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = "hyeong.lee.myboard.controller")
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        log.error("[MethodArgumentNotValidException] handle -> {}", exception.getObjectName());

        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                messageSource.getMessage("MethodArgumentNotValidException", null, request.getLocale()));
    }


    /**
     * BindException 처리
     * 입력 폼 데이터 검증에 실패한 경우에 대한 처리
     */
    @ExceptionHandler(BindException.class)
    public void handleBindException(BindException exception,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws IOException {
        log.error("[BindException] handle -> {}", exception.getObjectName());

        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                messageSource.getMessage("BindException", null, request.getLocale()));
    }


    /**
     * AccessDeniedException 처리
     * 접근 권한이 없는 경우 FORBIDDEN(403) 코드와 함께 반환
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException exception,
                                                   HttpServletResponse response) throws IOException {
        log.error("[AccessDeniedException] handle -> {}", exception.getMessage());

        response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
    }


    /**
     * 런타임 예외에 대한 처리
     * EntityNotFoundException, IllegalArgumentException 등등 나머지 전부 처리
     * (예외 종류가 많다보니 국제화 적용 X)
     */
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException exception,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        log.error("[RuntimeException] handle -> {}", exception.getMessage());

        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                messageSource.getMessage(exception.getMessage(), null, request.getLocale()));
    }

    /**
     * 기타 처리되지 못한 모든 Exception에 대한 처리
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        log.error("[Exception] handle -> {}", exception.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                messageSource.getMessage("Exception", null, request.getLocale()));
    }
}
