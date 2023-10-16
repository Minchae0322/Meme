package minchae.meme.service;

import minchae.meme.request.SignupForm;

public interface UserService {
    void signup(SignupForm signupForm);

    void changeAuth(Long userId, String auth);

    void changeNickName(Long userId, String nickName);

    boolean isExistEmail(String email);

    boolean isExistNickname(String nickname);

    boolean isValidNickname(String nickname);


}
