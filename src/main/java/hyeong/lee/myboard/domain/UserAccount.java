package hyeong.lee.myboard.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

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

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(length = 15, nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
