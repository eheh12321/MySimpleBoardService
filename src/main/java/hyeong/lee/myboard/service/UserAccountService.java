package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.UserSignUpRequestDto;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void create(UserSignUpRequestDto dto) {
        UserAccount userAccount = dto.toEntity(passwordEncoder); // Password는 BCrypt 암호화 이후 저장
        userAccountRepository.save(userAccount);
    }
}
