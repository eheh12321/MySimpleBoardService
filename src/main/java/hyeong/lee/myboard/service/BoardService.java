package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.dto.BoardRequestDto;
import hyeong.lee.myboard.dto.BoardResponseDto;
import hyeong.lee.myboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Long create(BoardRequestDto dto) {
        Board board = dto.toEntity();
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> findAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::from);
    }

    @Transactional(readOnly = true)
    public BoardResponseDto findById(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardResponseDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다 -> " + boardId));
    }
}
