package hyeong.lee.myboard.domain;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount extends AuditingFields implements Persistable<String> {

    @Id @Column(length = 30)
    private String userId;

    @Column(nullable = false, unique = true)
    private String userPassword;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 15, nullable = false, unique = true)
    private String nickname;

    @Column(length = 15, nullable = false, unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private String auth;

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

    // Persistable Implements

    @Override
    public String getId() {
        return userId;
    }

    @Override // 이미 존재하는 엔티티인지 검증
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
