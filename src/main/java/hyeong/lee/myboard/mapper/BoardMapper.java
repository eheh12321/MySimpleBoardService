package hyeong.lee.myboard.mapper;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.service.BoardService;
import org.mapstruct.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface BoardMapper  {

    // DTO -> UserAccount Entity
    UserAccount userAccountDtoToUserAccountEntity(UserAccountDto userAccountDto);

    // DTOs -> Board Entity
    default Board BoardPostDtoToBoardEntity(BoardRequest.BoardPostDto boardPostDto, UserAccountDto userAccountDto, PasswordEncoder passwordEncoder) {
        UserAccount userAccount = userAccountDtoToUserAccountEntity(userAccountDto);
        // 비밀글 체크를 한 경우에만 비밀번호 설정 (암호화 후 저장)
        String password = boardPostDto.getSecret() ? passwordEncoder.encode(boardPostDto.getPassword()) : null;

        return Board.builder()
                .title(boardPostDto.getTitle())
                .editor(boardPostDto.getEditor())
                .content(boardPostDto.getContent())
                .password(password)
                .userAccount(userAccount)
                .build();
    }
}
