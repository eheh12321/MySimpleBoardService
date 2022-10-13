package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.domain.type.SearchType;
import hyeong.lee.myboard.dto.BoardResponseDto;
import hyeong.lee.myboard.service.BoardService;
import hyeong.lee.myboard.service.PagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

        Page<BoardResponseDto> boards = boardService.searchBoards(searchType, searchValue, pageable);
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
        BoardResponseDto dto = boardService.readById(boardId);
        model.addAttribute("board", dto);

        return "board/board-detail";
    }

    @GetMapping("/edit/{boardId}")
    public String editBoard(@PathVariable Long boardId, Model model) {
        BoardResponseDto dto = boardService.readById(boardId);
        model.addAttribute("board", dto);

        return "board/board-edit";
    }
}
