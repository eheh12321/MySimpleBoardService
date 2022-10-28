package hyeong.lee.myboard.mapper;

import hyeong.lee.myboard.domain.Board;
import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.BoardRequest;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper  {

    // DTO -> UserAccount Entity
    UserAccount userAccountDtoToUserAccountEntity(UserAccountDto userAccountDto);

    // DTOs -> Board Entity
    default Board BoardPostDtoToBoardEntity(BoardRequest.BoardPostDto boardPostDto, UserAccountDto userAccountDto) {
        UserAccount userAccount = userAccountDtoToUserAccountEntity(userAccountDto);

        return Board.builder()
                .title(boardPostDto.getTitle())
                .editor(boardPostDto.getEditor())
                .content(boardPostDto.getContent())
                .userAccount(userAccount)
                .build();
    }
}
