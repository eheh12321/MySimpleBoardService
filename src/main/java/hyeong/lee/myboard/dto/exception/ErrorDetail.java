package hyeong.lee.myboard.dto.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Builder
@Data
public class ErrorDetail {
    private String objectName; // 에러가 발생한 객체 이름
    private String field; // 에러 발생 필드 이름
    private String code; // 어떤 타입의 에러인지 (ex: Size, NotBlank...)
    private String message; // 에러 메세지

    public static ErrorDetail from(FieldError e, MessageSource messageSource, Locale locale) {
        return ErrorDetail.builder()
                .objectName(e.getObjectName())
                .field(e.getField())
                .code(e.getCode())
                .message(messageSource.getMessage(e, locale))
                .build();
    }
}
