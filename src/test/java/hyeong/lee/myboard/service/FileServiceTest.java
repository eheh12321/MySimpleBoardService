package hyeong.lee.myboard.service;

import hyeong.lee.myboard.config.MyProperties;
import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UploadFile;
import hyeong.lee.myboard.repository.UploadFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService sut;

    @Mock
    private MyProperties myProperties;

    @Mock
    private UploadFileRepository uploadFileRepository;

    /**
     * 첨부파일 디렉토리 생성 테스트
     * - private 메서드이므로 리플렉션을 이용해 강제로 호출해서 테스트
     * - \yyyy\MM\dd\ 포맷으로 잘 생성되는지 테스트 (4-2-2)
     */
    @Test
    void 첨부파일_디렉토리_생성() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // When
        Method method = FileService.class.getDeclaredMethod("getDateDirectory");
        method.setAccessible(true); // private 메서드 호출
        String result = (String) method.invoke(sut);

        // Then
        // File.seperator -> "@" 으로 변환한 다음에 자릿수가 잘 맞는지를 정규식으로 체크
        String regex = "^@\\d{4}@\\d{2}@\\d{2}@$";
        String replaceStr = result.replace(File.separator, "@");
        System.out.println(replaceStr);

        assertThat(replaceStr.matches(regex)).isTrue();
    }

    /**
     * 첨부파일 저장 테스트
     * - 사실 굳이 없어도 될 것 같음. 통합테스트에서나 의미가 있을 듯
     */
    @Test
    void 첨부파일_저장() throws IOException {
        // Given
        Board board = Board.builder().id(1L).build();
        MockMultipartFile file = new MockMultipartFile("file", new byte[]{123});
        given(myProperties.getResourcesPath()).willReturn("C:\\Users\\HOME\\Desktop\\Java_study\\MySimpleBoardService\\storage\\test");

        // When && Then
        sut.saveFile(board, file);
        then(uploadFileRepository).should().save(any(UploadFile.class));
    }
}