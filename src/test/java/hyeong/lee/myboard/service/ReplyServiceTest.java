package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.ReplyRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.mapper.BoardMapper;
import hyeong.lee.myboard.repository.BoardRepository;
import hyeong.lee.myboard.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @InjectMocks
    private ReplyService sut; // 테스트 대상

    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private BoardRepository boardRepository;

    @Spy
    private BoardMapper boardMapper = Mappers.getMapper(BoardMapper.class);

    /**
     * 댓글 생성 테스트
     * - 게시글 정보가 없으면 EntityNotFoundException 터지는지 검증
     */
    @Test
    void 댓글_생성_게시글_없음_예외발생() {
        // Given
        Long boardId = 123L;
        ReplyRequest replyRequest = new ReplyRequest(boardId, "editor", "content");
        UserAccountDto userAccountDto = createUserAccountDto("userId");

        given(boardRepository.findById(boardId)).willThrow(EntityNotFoundException.class);

        // When
        Throwable t = catchThrowable(() -> sut.create(replyRequest, userAccountDto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class);
        then(boardRepository).should().findById(boardId);
    }

    /**
     * 게시글 삭제 테스트
     * - 비회원이 작성한 글은 바로 삭제 되는지 검증
     * - 회원이 작성한 글은 작성자 정보가 일치할 때 삭제되는지 검증
     * - 정보가 불일치 하는 경우 예외 터지는지도 검증
     */
    @Test
    void 댓글_삭제_비회원() {
        // Given
        Long replyId = 1L;
        Board board = createBoard(1L);
        Reply reply = createReply(board, null);

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        // When
        sut.delete(replyId, null);

        // Then
        then(replyRepository).should().delete(reply);
    }

    @Test
    void 댓글_삭제_회원() {
        // Given
        Long replyId = 1L;
        UserAccountDto userAccountDto = createUserAccountDto("sameUser");

        UserAccount sameUser = createUserAccount("sameUser");
        Board board = createBoard(1L);
        Reply reply = createReply(board, sameUser);

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        // When
        sut.delete(replyId, userAccountDto);

        // Then
        then(replyRepository).should().findById(replyId);
    }

    @Test
    void 댓글_삭제_작성자_불일치_예외발생() {
        // Given
        Long replyId = 1L;
        UserAccountDto userAccountDto = createUserAccountDto("user");

        UserAccount diffUser = createUserAccount("diffUser");
        Board board = createBoard(1L);
        Reply reply = createReply(board, diffUser);

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        // When
        Throwable t = catchThrowable(() -> sut.delete(replyId, userAccountDto));

        // Then
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("IllegalArgumentException.diff_user");
        then(replyRepository).should().findById(replyId);
    }

    private Reply createReply(Board board, UserAccount userAccount) {
        return Reply.builder()
                .editor("editor")
                .content("content")
                .userAccount(userAccount)
                .board(board).build();
    }

    // 게시글 및 계정에 대한 세부 내용은 테스트에 중요한 사항이 아니므로 id만 남기고 생략
    private Board createBoard(Long boardId) {
        return Board.builder()
                .id(boardId).build();
    }

    private UserAccount createUserAccount(String userId) {
        return UserAccount.builder()
                .userId(userId).build();
    }

    private UserAccountDto createUserAccountDto(String userId) {
        return new UserAccountDto(userId, "userPW", "ADMIN", "email@com", "nickname", "999-1234-1234", "memo", 0);
    }
}