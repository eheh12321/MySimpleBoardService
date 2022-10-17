package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.request.UserSignUpRequestDto;
import hyeong.lee.myboard.service.UserAccountService;
import hyeong.lee.myboard.validator.SignUpUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final SignUpUserValidator signUpUserValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(signUpUserValidator);
    }

    @GetMapping("/sign-up")
    public String getSignUpForm() {
        return "sign-up";
    }

    // 회원가입
    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Validated @Valid UserSignUpRequestDto dto) {
        userAccountService.create(dto);
        return ResponseEntity.ok("OK");
    }
}
