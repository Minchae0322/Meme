package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.SignupForm;
import minchae.meme.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void signup() throws Exception {
        SignupForm signupForm = SignupForm.builder()
                .username("ffff")
                .email("jmcabc@naver.com1")
                .password("wjdals12")
                .phoneNum("01035573336")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupForm)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(1, userRepository.count());
    }


    @Test
    @DisplayName("user 로 로그인")
    public void login() throws Exception {
        SignupForm signupForm = SignupForm.builder()
                .username("ddd")
                .email("jmcabc@naver.com12")
                .password(passwordEncoder.encode("wjdals12"))
                .phoneNum("01035573336")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupForm)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login?username=ddd&password=wjdals12"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

    }

    @Test
    @DisplayName("로그인에 실패할 시 loginPage 로 redirect 된다.")
    public void loginUser() throws Exception {
        User user = User.builder()
                .username("fff")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("123")
                .enable(true)
                .authorizations(Authorization.ADMIN)
                .build();
        userRepository.save(user);


        //when login 이 성공했을때는 /로 redirect 된다. /의 권한은 USER 이다.
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login?username=ddd&password=wjdals112"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login"));
                //.andExpect(MockMvcResultMatchers.redirectedUrl());

    }

    @Test
    @DisplayName("admin 으로 로그인")
    public void auth() throws Exception {
        User user = User.builder()
                .username("jmcabc@naver.com")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.ADMIN)
                .build();
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login?username=jmcabc@naver.com&password=wjdals12"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));



    }


}