package hyeong.lee.myboard.service;

import hyeong.lee.myboard.dto.properties.MyProperties;
import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UploadFile;
import hyeong.lee.myboard.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FileService {

    private final UploadFileRepository uploadFileRepository;
    private final MyProperties properties;

    public void saveFile(Board board, MultipartFile file) throws IOException {

        String datePath = getDateDirectory();
        log.info("파일 저장 경로: {}", properties.getResourcesPath() + datePath);

        // 파일을 저장하기 위한 고유 이름 생성 (겹치면 안되기 때문에 UUID 이용)
        UUID uuid = UUID.randomUUID();
        String uniqueFileName = uuid + "_" + file.getOriginalFilename();

        log.info("파일 저장 >> Original_Name: {}, Unique_Name: {}, type: {}, size: {}",
                file.getOriginalFilename(), uniqueFileName, file.getContentType(), file.getSize());

        UploadFile uploadFile = UploadFile.builder()
                .board(board)
                .originalFileName(file.getOriginalFilename())
                .uniqueFileName(uniqueFileName)
                .filePath(File.separator + "storage" + datePath).build();

        // DB에 파일 저장
        uploadFileRepository.save(uploadFile);

        // 실제 서버에 파일 저장
        byte[] bytes = file.getBytes();
        String fullPath = properties.getResourcesPath() + datePath; // 절대경로
        Path path = Paths.get(fullPath + uniqueFileName);
        Files.write(path, bytes);
    }

    /**
     * 날짜에 따른 업로트 파일 경로 생성 (연-월-일)
     * - 디텍토리가 존재하지 않는다면 새로 생성함
     * - ex) /2022/10/15/
     */

    private String getDateDirectory() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String datePath =
                        File.separator
                        + sdf.format(date).replace("-", File.separator)
                        + File.separator;

        String fullPath = properties.getResourcesPath() + datePath; // 절대경로
        File file = new File(fullPath);
        if (!file.exists()) {
            log.info(">> 새 디렉토리를 생성했습니다: {}", Paths.get(fullPath).toUri());
            file.mkdirs(); // 디렉토리가 존재하지 않는다면 새로 생성
        }
        return datePath;
    }
}
