package hyeong.lee.myboard.repository;

import hyeong.lee.myboard.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, String> {
}
