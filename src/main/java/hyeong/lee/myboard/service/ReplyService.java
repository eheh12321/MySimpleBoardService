package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.ReplyRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.mapper.BoardMapper;
import hyeong.lee.myboard.repository.BoardRepository;
import hyeong.lee.myboard.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    private final BoardMapper boardMapper;

    public Long create(ReplyRequest replyRequest, UserAccountDto userAccountDto) {
        // (1) 게시글 Entity 조회
        Board board = boardRepository.findById(replyRequest.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("EntityNotFoundException.board"));

        // (2) DTO -> Entity 변환
        UserAccount userAccount = boardMapper.userAccountDtoToUserAccountEntity(userAccountDto);
        Reply reply = boardMapper.replyRequestDtoToReplyEntity(replyRequest, userAccount, board);

        // (3) Entity 저장 후 ID 반환
        Reply savedReply = replyRepository.save(reply);
        return savedReply.getId();
    }

    public void delete(Long replyId, UserAccountDto userAccountDto) {
        // (1) 댓글 Entity 조회
        Reply reply = findById(replyId);

        // (2) DTO -> Entity 변환 (* userAccount가 null일 수 있음 *)
        UserAccount userAccount = boardMapper.userAccountDtoToUserAccountEntity(userAccountDto);

        // (3) 익명 회원이 작성한 댓글은 바로 삭제 가능
        if(reply.getUserAccount() == null) {
            replyRepository.delete(reply);
        } else {
            // (4) 회원이 작성한 댓글은 작성자 정보와 일치하는 경우에만 삭제 가능
            if(reply.getUserAccount().equals(userAccount)) {
                replyRepository.delete(reply);
            } else {
                throw new IllegalArgumentException("IllegalArgumentException.diff_user");
            }
        }
    }

    @Transactional(readOnly = true)
    public Reply findById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("EntityNotFoundException.reply"));
    }
}
