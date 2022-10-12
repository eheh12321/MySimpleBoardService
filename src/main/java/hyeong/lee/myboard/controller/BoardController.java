package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.BoardResponseDto;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/boards")
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String index(Pageable pageable, Model model) {
        Page<BoardResponseDto> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);

        return "index";
    }
}
