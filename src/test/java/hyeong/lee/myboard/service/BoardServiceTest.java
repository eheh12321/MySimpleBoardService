package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.mapper.BoardMapper;
import hyeong.lee.myboard.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService sut; // 테스트 대상

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private FileService fileService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy // 이 테스트 코드에서 직접 사용되지는 않지만 sut 메서드 호출 시에 내부에서 실제 기능이 동작하는 용도
    private BoardMapper boardMapper;

    /**
     * 게시글 생성 테스트
     * - 각 파일마다 저장 메서드 호출되는지 확인
     * - repository.save() 호출하는지 확인
     */
    @Test
    void 게시글_생성() throws IOException {
        // Given
        // 첨부파일 3개
        MultipartFile[] multipartFiles = new MultipartFile[]{
                new MockMultipartFile("file1", new byte[]{123}),
                new MockMultipartFile("file2", new byte[]{123}),
                new MockMultipartFile("file3", new byte[]{123})
        };

        // 생성할 게시글 정보를 담은 DTOs
        BoardRequest.BoardPostDto boardPostDto =
                new BoardRequest.BoardPostDto("title", "editor", "content", null, false, multipartFiles);
        UserAccountDto userAccountDto = createUserAccountDto("userId");

        // 저장된 엔티티
        Board savedEntity = createBoard(createUserAccount("userId"), null);

        given(boardRepository.save(any(Board.class))).willReturn(savedEntity);
        willDoNothing().given(fileService).saveFile(any(Board.class), any(MultipartFile.class));

        // When
        Long savedId = sut.create(boardPostDto, userAccountDto);

        // Then
        assertThat(savedId).isEqualTo(1L);
        then(boardRepository).should().save(any(Board.class));
        verify(fileService, times(3)).saveFile(any(Board.class), any(MultipartFile.class));
    }


    /**
     * 게시글 수정 테스트
     * - 비회원이 작성했을 경우 AccessDeniedException 터지는지 검증
     * - 사용자 정보가 일치하지 않는 경우 IllegalArgumentException 터지는지 검증
     */
    @Test
    void 게시글_수정_비회원_예외발생() {
        // Given
        // 수정을 위한 데이터
        Long boardId = 1L;
        BoardRequest.BoardPatchDto boardPatchDto = new BoardRequest.BoardPatchDto("newTitle", "newContent");
        UserAccountDto userAccountDto = null;

        // 기존에 저장된 게시글 데이터
        Board board = createBoard(null, null);
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        // When
        Throwable t = catchThrowable(() -> sut.update(boardId, boardPatchDto, userAccountDto));

        // Then
        assertThat(t)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("AccessDeniedException");
        then(boardRepository).should().findById(boardId);
    }

    @Test
    void 게시글_수정_사용자_불일치_예외발생() {
        // Given
        Long boardId = 1L;
        BoardRequest.BoardPatchDto boardPatchDto = new BoardRequest.BoardPatchDto("newTitle", "newContent");
        UserAccountDto userAccountDto = createUserAccountDto("userId");

        UserAccount differentUserAccount = createUserAccount("diffUserId");
        Board board = createBoard(differentUserAccount, null);
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        // When
        Throwable t = catchThrowable(() -> sut.update(boardId, boardPatchDto, userAccountDto));

        // Then
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("IllegalArgumentException.diff_user");
        then(boardRepository).should().findById(boardId);
    }


    /**
     * 게시글 상세 조회 테스트
     * - 없는 게시글을 조회하려 할 때 EntityNotFoundException 터지는지 검증
     * - 비밀글 조회 시 입력 비밀번호가 없으면 AccessDeniedException 터지는지 검증
    */
    @Test
    void 게시글_조회_없는글_예외발생() {
        // Given
        Long boardId = 123L;
        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.readWithRepliesById(boardId, null));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("EntityNotFoundException.board");
        then(boardRepository).should().findById(boardId);
    }

    @Test
    void 게시글_조회_비밀글_비밀번호없음_예외발생() {
        // Given
        Long boardId = 1L;

        Board findBoard = createBoard(null, "boardPassword"); // 비밀글
        given(boardRepository.findById(boardId)).willReturn(Optional.of(findBoard));

        // When
        Throwable t = catchThrowable(() -> sut.readWithRepliesById(boardId, null));

        // Then
        assertThat(t)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("AccessDeniedException.board");
    }

    /**
     * 게시글 삭제 테스트
     * - 작성자 정보가 일치하지 않으면 IllegalArgumentException 터지는지 검증
     */
    @Test
    void 게시글_삭제_사용자_불일치_예외발생() {
        // Given
        Long boardId = 1L;
        UserAccountDto userAccountDto = createUserAccountDto("user");

        UserAccount diffUser = createUserAccount("diffUser");
        Board board = createBoard(diffUser, null);

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        // When
        Throwable t = catchThrowable(() -> sut.delete(boardId, userAccountDto));

        // Then
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("IllegalArgumentException.diff_user");
        then(boardRepository).should().findById(boardId);
    }

    private Board createBoard(UserAccount userAccount, String password) {
        return Board.builder()
                .id(1L)
                .title("title")
                .content("content")
                .editor("editor")
                .userAccount(userAccount)
                .replies(null)
                .uploadFiles(null)
                .password(password).build();
    }

    private UserAccount createUserAccount(String userId) {
        return UserAccount.builder()
                .userId(userId)
                .userPassword("userPw")
                .auth("ADMIN")
                .email("email@com")
                .phoneNumber("999-1111-2222")
                .memo("memo").build();
    }

    private UserAccountDto createUserAccountDto(String userId) {
        return new UserAccountDto(userId, "userPW", "ADMIN", "email@com", "nickname", "999-1234-1234", "memo", 0);
    }
}
