package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.ReplyRequestDto;
import hyeong.lee.myboard.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ReplyRequestDto dto) {
        Long replyId = replyService.create(dto);
        return ResponseEntity.ok(replyId);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Long> delete(@PathVariable Long replyId) {
        replyService.delete(replyId);
        return ResponseEntity.ok(replyId);
    }
}
