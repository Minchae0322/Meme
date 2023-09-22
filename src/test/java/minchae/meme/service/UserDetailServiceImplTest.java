/*
package minchae.meme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.SignupForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserDetailServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void signup() throws Exception {
        SignupForm signupForm = SignupForm.builder()
                .username("ddd")
                .email("jmcabc@naver.com1")
                .password("wjdals12")
                .phoneNum("01035573336")
                .build();

        userService.signup(signupForm);

        assertEquals(1, userRepository.count());
    }

}*/
