package minchae.meme.service;

import minchae.meme.request.Login;
import minchae.meme.request.SignupForm;
import org.springframework.stereotype.Service;

public interface UserService {
    void signup(SignupForm signupForm);

}
