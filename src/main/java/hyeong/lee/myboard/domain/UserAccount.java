package hyeong.lee.myboard.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount {

    @Id @Column(length = 30)
    private String userId;

    @Column(length = 30, nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String email;

    @Column(length = 30)
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String memo;

}
