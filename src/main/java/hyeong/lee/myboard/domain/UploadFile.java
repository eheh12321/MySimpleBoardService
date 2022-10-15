package hyeong.lee.myboard.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UploadFile {

    @Id
    private String uniqueFileName; // 저장되는 고유 식별용 이름 (original_uuid)

    private String originalFileName; // 원본 파일 이름

    @ManyToOne(optional = false)
    private Board board;

    private String filePath; // 파일 저장 경로
}
