package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.User;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.exception.IsExistedUser;
import minchae.meme.exception.IsNotExistUser;
import minchae.meme.exception.IsWrongIdAndPassword;
import minchae.meme.exception.Unauthorized;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.SignupForm;
import minchae.meme.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo 비밀번호를 jwt 토큰으로 가져오기
        return userRepository.findByUsername(username)
                .orElseThrow(IsWrongIdAndPassword::new);
    }

    @Override
    @Transactional
    public void signup(SignupForm signupForm) {
        if (userRepository.findByUsername(signupForm.getUsername()).isPresent()) {
            throw new IsExistedUser();
        }
        User user = User.builder()
                .username(signupForm.getUsername())
                .email(signupForm.getEmail())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeAuth(Long userId, String auth) {
        User user = userRepository.findById(userId)
                .orElseThrow(IsNotExistUser::new);
        user.changeAuth(Authorization.valueOf(auth));
    }

    @Override
    @Transactional
    public void changeNickName(Long userId, String nickName) {
        User user = userRepository.findById(userId)
                .orElseThrow(IsNotExistUser::new);
        user.setNickName(nickName);
    }
}
