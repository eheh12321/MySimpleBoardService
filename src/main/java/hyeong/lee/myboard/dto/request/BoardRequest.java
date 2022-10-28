package hyeong.lee.myboard.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// DTO 내에서 Setter 사용은 지양한다!!
// DTO <-> Entity 변환은 Mapper에서 진행한다!!
// BoardRequest 관련한 DTO를 한곳에서 모아 볼 수 있게끔 하기 위해 내부 클래스로 관리!

public class BoardRequest {

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED) // ObjectMapper(jackson) 는 NoArgs 생성자가 반드시 필요하다
    @Getter // 리플렉션을 이용해 주입하기 때문에 @Setter 필요 없음
    public static class BoardPostDto {
        @Size(max = 50)
        @NotBlank
        private String title;

        @Size(max = 15)
        @NotBlank
        private String editor;

        @NotBlank
        private String content;

        private MultipartFile[] files;
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class BoardPatchDto {
        @Size(max = 50)
        @NotBlank
        private String title;

        @NotBlank
        private String content;
    }
}
