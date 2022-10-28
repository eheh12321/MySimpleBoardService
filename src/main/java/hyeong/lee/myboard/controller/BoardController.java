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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                        @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
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
    public String boardDetail(@PathVariable Long boardId, Model model) {
        BoardWithRepliesResponseDto dto = boardService.readWithRepliesById(boardId);
        model.addAttribute("board", dto);
        model.addAttribute("replies", dto.getReplies());

        return "board/board-detail";
    }

    @GetMapping("/edit/{boardId}")
    public String editBoard(@AuthenticationPrincipal BoardPrincipal boardPrincipal,
                            @PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId);

        if(boardPrincipal == null || board.getUserAccount() == null) { // 비로그인 상태거나, 익명이 작성한 글은 수정 불가
            throw new AccessDeniedException("AccessDeniedException.Login");
        } else if (!board.getUserAccount().getUserId().equals(boardPrincipal.getUsername())) { // 게시글 작성자 정보가 로그인 정보와 일치하지 않으면 수정불가
            throw new AccessDeniedException("AccessDeniedException");
        }

        BoardResponse dto = BoardResponse.from(board);
        model.addAttribute("board", dto);

        return "board/board-edit";
    }
}
