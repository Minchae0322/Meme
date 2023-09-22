package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.exception.IsExistEmail;
import minchae.meme.request.SignupForm;
import minchae.meme.request.VerificationRequest;
import minchae.meme.service.AuthService;
import minchae.meme.service.UserService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/auth/isValidToken")
    public void isValidToken() {

    }

    @PostMapping("/auth/sendMail")
    public void sendMail(@RequestBody() VerificationRequest request) {
        if (userService.isExistEmail(request.getSubject())) {
            throw new IsExistEmail();
        }
        authService.sendVerificationCode(request.getSubject());
    }

    @PostMapping("/auth/mailVerify")
    public void verifyMail(@RequestBody() VerificationRequest request) {
        if (!authService.isVerified(request)) {
            throw new IllegalArgumentException();
        }
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupForm signupForm) {
        userService.signup(signupForm);
    }


    @PostMapping("/admin/changeAuth")
    public String changeAuth() {
        return "username";
    }

    @GetMapping("/admin")
    public String admin() {
        return "username";
    }

    @GetMapping("/home")
    public String home() {
        return "홈 입니다";
    }

    @PostMapping("/auth/logout")
    public String logout(String username, String password) {


        return "logout";
    }





}
