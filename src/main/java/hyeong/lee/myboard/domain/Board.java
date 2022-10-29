package hyeong.lee.myboard.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 15, nullable = false)
    private String editor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String password; // 비밀글 조회 비밀번호 (null 유무로 비밀글 유무 체크)

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    @ManyToOne
    private UserAccount userAccount;

    public void updateContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
