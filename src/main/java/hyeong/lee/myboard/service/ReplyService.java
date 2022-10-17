package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.ReplyRequestDto;
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

    private final BoardService boardService;
    private final ReplyRepository replyRepository;

    public Long create(ReplyRequestDto dto) {
        Board board = boardService.findById(dto.getBoardId());
        Reply savedReply = replyRepository.save(dto.toEntity(board));

        return savedReply.getId();
    }

    public void delete(Long replyId, UserAccount userAccount) {
        Reply reply = findById(replyId);

        // 익명 회원이 작성한 댓글은 일단 모두 삭제 가능
        if(reply.getUserAccount() == null) {
            replyRepository.delete(reply);
            return;
        }

        // 작성자 정보와 일치하는 경우에만 삭제 가능
        if(reply.getUserAccount().equals(userAccount)) {
            replyRepository.delete(reply);
        } else {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않습니다");
        }
    }

    @Transactional(readOnly = true)
    public Reply findById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
    }
}
