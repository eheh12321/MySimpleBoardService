package hyeong.lee.myboard.domain.type;

import lombok.Getter;

public enum SearchType {

    TITLE("제목"),
    CONTENT("본문"),
    EDITOR("작성자");

    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
