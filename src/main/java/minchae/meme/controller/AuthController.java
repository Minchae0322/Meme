package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.User;
import minchae.meme.request.SignupForm;
import minchae.meme.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupForm signupForm) {
        userService.signup(signupForm);
    }


    @PostMapping("/auth/login")
    public String login() {
        return "username";
    }

    @GetMapping("/")
    public String home() {
        return "로그인 성공";
    }







}
