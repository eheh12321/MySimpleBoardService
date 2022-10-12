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

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public void create(BoardRequestDto dto) {
        Board board = dto.toEntity();
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> findAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::from);
    }
}
