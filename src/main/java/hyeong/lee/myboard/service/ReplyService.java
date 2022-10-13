package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.dto.ReplyRequestDto;
import hyeong.lee.myboard.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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

    public void delete(Long replyId) {
        replyRepository.delete(findById(replyId));
    }

    @Transactional(readOnly = true)
    public Reply findById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다 -> " + replyId));
    }
}
