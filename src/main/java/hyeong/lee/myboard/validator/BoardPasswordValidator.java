package hyeong.lee.myboard.validator;

import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@RequiredArgsConstructor
@Component
public class BoardPasswordValidator implements Validator {

    private final BoardService boardService;

    @Override
    public boolean supports(Class<?> clazz) {
        return BoardRequest.BoardPasswordDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardRequest.BoardPasswordDto boardPasswordDto = (BoardRequest.BoardPasswordDto) target;

        // 입력한 게시글 비밀번호가 저장된 게시글 비밀번호와 일치하는지 유무 확인
        boolean matchPassword
                = boardService.isMatchSecretPassword(boardPasswordDto.getBoardId(), boardPasswordDto.getPassword());

        if (!matchPassword) { // 일치하지 않으면?
            errors.rejectValue("password", "비밀번호 검증 오류", "비밀번호가 일치하지 않습니다");
        }
    }
}

