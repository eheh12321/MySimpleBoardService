package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.BoardRequestDto;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody BoardRequestDto dto) {
        Long boardId = boardService.create(dto);
        return ResponseEntity.ok(boardId);
    }
}
