package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.entity.TokenInfo;
import minchae.meme.entity.User;
import minchae.meme.request.SignupForm;
import minchae.meme.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/auth/isValidToken")
    public void isValidToken() {

    }


    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupForm signupForm) {
        userService.signup(signupForm);
    }


  /*  @PostMapping("/auth/login")
    public TokenInfo login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }*/



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
