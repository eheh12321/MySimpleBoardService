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

    @Transactional(readOnly = true) // 단건 읽기
    public BoardResponseDto readById(Long boardId) {
        return BoardResponseDto.from(findById(boardId));
    }

    @Transactional(readOnly = true) // 전체 목록 읽기
    public Page<BoardResponseDto> readAll(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::from);
    }

    public Long create(BoardRequestDto dto) {
        Board board = dto.toEntity();
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }

    public void update(Long boardId, BoardRequestDto dto) {
        Board board = findById(boardId);
        board.updateContent(dto.getTitle(), dto.getContent());
    }

    public void delete(Long boardId) {
        Board board = findById(boardId);
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다 -> " + boardId));
    }
}
