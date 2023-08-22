package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.User;
import minchae.meme.request.SignupForm;
import minchae.meme.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupForm signupForm) {
        userService.signup(signupForm);
    }


    @PostMapping("/auth/login")
    public String login(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }


    @PostMapping("/admin/changeAuth")
    public String changeAuth() {
        return "username";
    }

    @GetMapping("/admin")
    public String admin() {
        return "username";
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }







}
