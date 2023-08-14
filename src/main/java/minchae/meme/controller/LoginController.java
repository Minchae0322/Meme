package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.auth.Encode;
import minchae.meme.auth.impl.Encode_JWT;
import minchae.meme.auth.impl.Encode_accessToken;
import minchae.meme.entity.Session;
import minchae.meme.entity.User;
import minchae.meme.repository.SessionRepository;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.Login;
import minchae.meme.request.SignupForm;
import minchae.meme.service.SessionService;
import minchae.meme.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final SessionService sessionService;

    @PostMapping("/auth/login")
    public Login login(@RequestBody Login login) {
        User user = null;
        Encode encode = new Encode_JWT();
        Session session = Session.builder()
                .accessToken(encode.encodeValue(user.getId()))
                //.user(user)
                .build();
        sessionService.writeSession(session);
        return login;
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupForm signupForm) {
        userService.signup(signupForm);
    }
}
