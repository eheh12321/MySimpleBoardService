package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.dto.response.BoardResponse;
import hyeong.lee.myboard.dto.response.BoardWithRepliesResponseDto;
import hyeong.lee.myboard.mapper.BoardMapper;
import hyeong.lee.myboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final FileService fileService;
    private final BoardRepository boardRepository;

    private final BoardMapper boardMapper;
    private final PasswordEncoder passwordEncoder; // BCryptEncoder


    @Transactional(readOnly = true) // 비밀글 유무 리턴
    public boolean isSecretBoard(Long boardId) {
        Board board = findById(boardId);
        return board.getPassword() != null;
    }

    @Transactional(readOnly = true) // 비밀글 비밀번호 맞는지 유무 검증
    public boolean isMatchSecretPassword(Long boardId, String rawPassword) {
        Board board = findById(boardId);
        return passwordEncoder.matches(rawPassword, board.getPassword());
    }

    @Transactional(readOnly = true) // 댓글 정보와 함께 단건 읽기
    public BoardWithRepliesResponseDto readWithRepliesById(Long boardId, String password) {
        // (1) 게시글 조회
        Board board = findById(boardId);
        
        // (2) 만약 비밀글이라면 비밀번호 검증
        if(board.getPassword() != null) {
            if(password == null || !passwordEncoder.matches(password, board.getPassword())) {
                throw new AccessDeniedException("AccessDeniedException.board");
            }
        }
        // (3) 게시글 응답
        return BoardWithRepliesResponseDto.from(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponse> searchBoards(String searchType, String searchValue, Pageable pageable) {
        if(searchValue == null || searchValue.isBlank()) { // 검색어가 비어있으면 기본 정렬(ID 내림차순)값 반환
            return boardRepository.findAll(pageable).map(BoardResponse::from);
        }

        Page<BoardResponse> dto = null;
        switch(searchType) {
            case "EDITOR":
                dto = boardRepository.findByEditorContainingIgnoreCase(searchValue, pageable)
                        .map(BoardResponse::from); break;
            case "TITLE":
                dto = boardRepository.findAllByTitleContainingIgnoreCase(searchValue, pageable)
                        .map(BoardResponse::from); break;
            case "CONTENT":
                dto = boardRepository.findAllByContentContainingIgnoreCase(searchValue, pageable)
                        .map(BoardResponse::from); break;
        }
        return dto;
    }

    public Long create(BoardRequest.BoardPostDto boardPostDto, UserAccountDto userAccountDto) {
        // (1) Dto -> Entity 변환
        Board board = boardMapper.BoardPostDtoToBoardEntity(boardPostDto, userAccountDto, passwordEncoder);

        // (2) Entity 저장
        Board savedBoard = boardRepository.save(board);

        // (3) 첨부파일이 있는경우 저장
        try {
            for (MultipartFile file : boardPostDto.getFiles()) {
                if(!file.isEmpty()) {
                    fileService.saveFile(board, file);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("IllegalStateException.save_file");
        }

        // (4) 생성된 게시글의 ID 반환
        return savedBoard.getId();
    }

    public void update(Long boardId, BoardRequest.BoardPatchDto boardPatchDto, UserAccountDto userAccountDto) {
        // (1) Board Entity 조회
        Board board = findById(boardId);
        if(board.getUserAccount() == null) { // 비회원이 작성한 글은 수정 불가
            throw new AccessDeniedException("AccessDeniedException");
        }

        // (2) UserAccount DTO -> Entity 변환
        UserAccount loginUser = userAccountDto.toEntity();

        // (3) 글 작성자가 현재 로그인 계정과 일치하는 경우에만 수정 가능
        if(board.getUserAccount().equals(loginUser)) {
            board.updateContent(boardPatchDto.getTitle(), boardPatchDto.getContent());
        } else {
            throw new IllegalArgumentException("IllegalArgumentException.diff_user");
        }
    }

    public void delete(Long boardId, UserAccountDto userAccountDto) {
        // (1) 게시글 조회
        Board board = findById(boardId);

        // (2) UserAccount DTO -> Entity 변환
        UserAccount userAccount = userAccountDto.toEntity();

        // (3) 익명 회원이 작성한 글은 모두 삭제 가능
        if(board.getUserAccount() == null) {
            boardRepository.delete(board);
            return;
        }

        // (4) 회원이 작성한 글은 작성자 정보와 일치하는 경우에만 삭제 가능
        if(board.getUserAccount().equals(userAccount)) {
            boardRepository.delete(board);
        } else {
            throw new IllegalArgumentException("IllegalArgumentException.diff_user");
        }
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("EntityNotFoundException.board"));
    }
}
