package minchae.meme.service;

import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.request.Login;
import minchae.meme.request.SignupForm;
import org.springframework.stereotype.Service;

public interface UserService {
    void signup(SignupForm signupForm);

    void changeAuth(Long userId, String auth);


}
