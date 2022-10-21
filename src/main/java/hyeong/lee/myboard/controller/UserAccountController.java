package hyeong.lee.myboard.controller;

import hyeong.lee.myboard.dto.request.UserSignUpRequestDto;
import hyeong.lee.myboard.service.UserAccountService;
import hyeong.lee.myboard.validator.SignUpUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    protected void initBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(signUpUserValidator);
    }

    @GetMapping("/sign-up")
    public String getSignUpForm() {
        return "sign-up";
    }


    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequestDto dto) {
        userAccountService.create(dto);
        return ResponseEntity.ok("OK");
    }
}
