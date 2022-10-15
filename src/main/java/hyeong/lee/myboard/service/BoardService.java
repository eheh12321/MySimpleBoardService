package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.BoardRequestDto;
import hyeong.lee.myboard.dto.BoardResponseDto;
import hyeong.lee.myboard.dto.BoardWithRepliesResponseDto;
import hyeong.lee.myboard.repository.BoardRepository;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final FileService fileService;
    private final BoardRepository boardRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true) // 단건 읽기
    public BoardResponseDto readById(Long boardId) {
        return BoardResponseDto.from(findById(boardId));
    }

    @Transactional(readOnly = true) // 댓글 정보와 함께 단건 읽기
    public BoardWithRepliesResponseDto readWithRepliesById(Long boardId) {
        return BoardWithRepliesResponseDto.from(findById(boardId));
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> searchBoards(String searchType, String searchValue, Pageable pageable) {
        if(searchValue == null || searchValue.isBlank()) { // 검색어가 비어있으면 기본 정렬(ID 내림차순)값 반환
            return boardRepository.findAll(pageable).map(BoardResponseDto::from);
        }

        Page<BoardResponseDto> dto = null;
        switch(searchType) {
            case "EDITOR":
                dto = boardRepository.findByEditorContainingIgnoreCase(searchValue, pageable)
                        .map(BoardResponseDto::from); break;
            case "TITLE":
                dto = boardRepository.findAllByTitleContainingIgnoreCase(searchValue, pageable)
                        .map(BoardResponseDto::from); break;
            case "CONTENT":
                dto = boardRepository.findAllByContentContainingIgnoreCase(searchValue, pageable)
                        .map(BoardResponseDto::from); break;
        }
        return dto;
    }

    public Long create(BoardRequestDto dto) {
        Board board = dto.toEntity();
        Board savedBoard = boardRepository.save(board);
        try {
            // 첨부파일이 있는경우 저장 (첨부파일이 없는 경우 NULL이 오는 것이 아니라 길이 1짜리 빈배열이 전달됨)
            if(!(dto.getFiles().length == 1 && dto.getFiles()[0].isEmpty())) {
                fileService.saveFile(board, dto.getFiles());
            }
        } catch (IOException e) {
            log.error(">> 파일 저장에 실패했습니다.");
        }
        return savedBoard.getId();
    }

    public void update(Long boardId, BoardRequestDto dto) {
        // 실제 값이 필요하므로 findById
        Board board = findById(boardId);
        // 식별자(userId)에 대한 값만 필요하므로 getById
        UserAccount findUser = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());

        if(board.getUserAccount() == null) {
            log.error("비회원이 작성한 글은 수정할 수 없습니다"); return;
        }

        // 작성자가 일치하는 경우에만 수정 가능
        if(board.getUserAccount().equals(findUser)) {
            board.updateContent(dto.getTitle(), dto.getContent());
        } else {
            log.error(">> 사용자 정보가 일치하지 않습니다");
        }
    }

    public void delete(Long boardId, UserAccount userAccount) {
        Board board = findById(boardId);

        // 익명 회원이 작성한 글은 일단 모두 삭제 가능
        if(board.getUserAccount() == null) {
            boardRepository.delete(board);
            return;
        }

        // 작성자 정보와 일치하는 경우에만 삭제 가능
        if(board.getUserAccount().equals(userAccount)) {
            boardRepository.delete(board);
        } else {
            log.error(">> 사용자 정보가 일치하지 않습니다");
        }
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다 -> " + boardId));
    }
}
