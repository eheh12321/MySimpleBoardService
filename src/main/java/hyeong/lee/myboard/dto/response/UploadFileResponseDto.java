package hyeong.lee.myboard.dto.response;

import hyeong.lee.myboard.domain.UploadFile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileResponseDto {

    private final String uniqueFileName;
    private final String originalFileName;
    private final String filePath;

    public static UploadFileResponseDto from(UploadFile file) {
        return UploadFileResponseDto.builder()
                .uniqueFileName(file.getUniqueFileName())
                .originalFileName(file.getOriginalFileName())
                .filePath(file.getFilePath()).build();
    }
}
