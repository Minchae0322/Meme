package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.Login;
import minchae.meme.request.SignupForm;
import minchae.meme.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/auth/login")
    public Login login(@RequestBody Login login) {
        return login;
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupForm signupForm) {
        userService.signup(signupForm);
    }
}
