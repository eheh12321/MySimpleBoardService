package hyeong.lee.myboard.service;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.UserSignUpRequestDto;
import hyeong.lee.myboard.event.CustomEventPublisher;
import hyeong.lee.myboard.event.UserRegisterEvent;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final CustomEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void create(UserSignUpRequestDto dto) {
        // 1. 회원 생성
        UserAccount userAccount = dto.toEntity(passwordEncoder); // Password는 BCrypt 암호화 이후 저장
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        // 2. 회원 생성 알림 발송 (비동기)
        UserRegisterEvent userRegisterEvent = new UserRegisterEvent(this, savedUserAccount);
        publisher.publishCustomEvent(userRegisterEvent);
    }

    @Transactional(readOnly = true)
    public UserAccount findByUserId(String userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
    }
}
