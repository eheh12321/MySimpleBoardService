package hyeong.lee.myboard.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ErrorResult {
    private String path; // 요쳥 경로
    private LocalDateTime timestamp; // 에러 발생 시각
    private int status; // 에러 코드
    private String error; // 에러명 (ex: Bad Request, Forbidden...)
    private List<ErrorDetail> errors; // 각 필드 에러 정보
    private String message; // 에러 메세지
}
