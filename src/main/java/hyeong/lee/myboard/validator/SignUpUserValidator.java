package hyeong.lee.myboard.validator;

import hyeong.lee.myboard.dto.request.UserSignUpRequestDto;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignUpUserValidator implements Validator {

    private final UserAccountRepository userAccountRepository;

    @Override // 해당 검증기를 지원하는지 체크
    public boolean supports(Class<?> clazz) {
        return UserSignUpRequestDto.class.isAssignableFrom(clazz);
    }

    @Override // 검증 진행
    public void validate(Object target, Errors errors) {
        UserSignUpRequestDto dto = (UserSignUpRequestDto) target;

        if(userAccountRepository.existsByEmail(dto.getEmail())) {
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일입니다");
        }
        if(userAccountRepository.existsByNickname(dto.getNickname())) {
            errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용중인 닉네임입니다");
        }
        if(userAccountRepository.existsByUserId(dto.getUserId())) {
            errors.rejectValue("userId", "아이디 중복 오류", "이미 사용중인 아이디입니다");
        }
        if(userAccountRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "전화번호 중복 오류", "이미 사용중인 전화번호입니다");
        }
    }
}