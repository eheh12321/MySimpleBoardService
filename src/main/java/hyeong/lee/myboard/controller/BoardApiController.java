package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.BoardRequestDto;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardRequestDto> create(BoardRequestDto dto) {
        boardService.create(dto);
        return ResponseEntity.ok(dto);
    }
}
