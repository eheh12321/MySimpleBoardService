package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.type.SearchType;
import hyeong.lee.myboard.dto.response.BoardResponse;
import hyeong.lee.myboard.dto.response.BoardWithRepliesResponseDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.service.BoardService;
import hyeong.lee.myboard.service.PagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
@Controller
public class BoardController {

    private final BoardService boardService;
    private final PagingService pagingService;

    @GetMapping
    public String index(@RequestParam(required = false) String searchType,
                        @RequestParam(required = false) String searchValue,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {

        Page<BoardResponse> boards = boardService.searchBoards(searchType, searchValue, pageable);
        List<Integer> paginationNumbers = pagingService.getPaginationNumbers(pageable.getPageNumber(), boards.getTotalPages());

        model.addAttribute("boards", boards);
        model.addAttribute("paginationNumbers", paginationNumbers);
        model.addAttribute("searchTypes", SearchType.values());

        return "board/board-list";
    }

    @GetMapping("/new")
    public String createBoard() {
        return "board/board-new";
    }

    @GetMapping("/{boardId}")
    public String boardDetail(@PathVariable Long boardId,
                              HttpSession session,
                              Model model) {
        // (1) 해당 게시글이 비밀글인지 확인
        boolean secretBoard = boardService.isSecretBoard(boardId);

        // (2) 세션에 해당 게시글에 대한 비밀번호가 저장되어 있는지 확인
        String savedPassword = (String) session.getAttribute("secret_board_" + boardId);

        // (3) 비밀글인데 세션에 비밀번호 정보가 없이 요청했다면 비밀번호 입력 페이지로 포워드
        if (secretBoard && savedPassword == null) {
            return "forward:/boards/" + boardId + "/auth";
        }

        // (4) 비밀글이 아니거나 세션에 비밀번호 정보가 있다면 정상 요청
        BoardWithRepliesResponseDto dto = boardService.readWithRepliesById(boardId, savedPassword);
        model.addAttribute("board", dto);
        model.addAttribute("replies", dto.getReplies());

        return "board/board-detail";
    }

    @GetMapping("/{boardId}/auth")
    public String getSecretBoardForm(@PathVariable Long boardId,
                                     HttpSession session,
                                     Model model) {

        // (1) 해당 게시글이 비밀글인지 확인
        boolean secretBoard = boardService.isSecretBoard(boardId);

        // (2) 세션에 해당 게시글에 대한 비밀번호가 저장되어 있는지 확인
        String savedPassword = (String) session.getAttribute("secret_board_" + boardId);

        // (3) 해당 글이 비밀글이 아닌 경우 or 세션에 비밀번호가 있는 경우 게시글 조회 페이지로 다시 리다이렉트
        // (URL으로 직접 치고 들어온 경우에 대한 처리 과정)
        if (!secretBoard || savedPassword != null) {
            return "redirect:/boards/" + boardId;
        }

        model.addAttribute("boardId", boardId);
        return "board/board-secret-auth";
    }

    @GetMapping("/edit/{boardId}")
    public String editBoard(@AuthenticationPrincipal BoardPrincipal boardPrincipal,
                            @PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId);

        if (boardPrincipal == null || board.getUserAccount() == null) { // 비로그인 상태거나, 익명이 작성한 글은 수정 불가
            throw new AccessDeniedException("AccessDeniedException.login");
        } else if (!board.getUserAccount().getUserId().equals(boardPrincipal.getUsername())) { // 게시글 작성자 정보가 로그인 정보와 일치하지 않으면 수정불가
            throw new AccessDeniedException("AccessDeniedException");
        }

        BoardResponse dto = BoardResponse.from(board);
        model.addAttribute("board", dto);

        return "board/board-edit";
    }
}
