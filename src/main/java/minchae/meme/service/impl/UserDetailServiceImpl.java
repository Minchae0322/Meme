package minchae.meme.service.impl;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.User;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.exception.IsExistedUser;
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
    public UserDetails loadUserByUsername(java.lang.String username) throws UsernameNotFoundException {
        //todo 비밀번호를 jwt 토큰으로 가져오기
        return userRepository.findByName(username)
                .orElseThrow(Unauthorized::new);
    }

    @Override
    public void signup(SignupForm signupForm) {
        if (userRepository.findByName(signupForm.getName()).isPresent()) {
            throw new IsExistedUser();
        }
        User user = User.builder()
                .name(signupForm.getName())
                .email(signupForm.getEmail())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();

        userRepository.save(user);
    }
}
