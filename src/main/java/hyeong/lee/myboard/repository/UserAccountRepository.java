package hyeong.lee.myboard.repository;

import hyeong.lee.myboard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    boolean existsByUserId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
