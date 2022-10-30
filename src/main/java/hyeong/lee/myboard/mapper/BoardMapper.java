package hyeong.lee.myboard.mapper;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.Reply;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.dto.request.ReplyRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import org.mapstruct.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface BoardMapper  {

    // DTO -> UserAccount Entity
    UserAccount userAccountDtoToUserAccountEntity(UserAccountDto userAccountDto);

    // DTOs -> Board Entity
    default Board boardPostDtoToBoardEntity(BoardRequest.BoardPostDto boardPostDto, UserAccount userAccount, PasswordEncoder passwordEncoder) {
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

    // DTO -> Reply Entity
    default Reply replyRequestDtoToReplyEntity(ReplyRequest replyRequest, UserAccount userAccount, Board board) {
        return Reply.builder()
                .editor(replyRequest.getEditor())
                .content(replyRequest.getContent())
                .board(board)
                .userAccount(userAccount).build();
    }
}
