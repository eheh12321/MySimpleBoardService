package hyeong.lee.myboard.restcontroller;

import hyeong.lee.myboard.dto.request.UserSignUpRequestDto;
import hyeong.lee.myboard.service.UserAccountService;
import hyeong.lee.myboard.validator.SignUpUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserAccountApiController {

    private final UserAccountService userAccountService;
    private final SignUpUserValidator signUpUserValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(signUpUserValidator);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequestDto dto) {
        userAccountService.create(dto);
        return ResponseEntity.ok("OK");
    }
}
