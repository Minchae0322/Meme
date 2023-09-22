package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.auth.VerificationProvider;
import minchae.meme.entity.User;
import minchae.meme.entity.VerificationToken;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.repository.VerificationTokenRepository;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.Login;
import minchae.meme.request.SignupForm;
import minchae.meme.request.VerificationRequest;
import minchae.meme.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    private VerificationProvider verificationProvider;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MessageSource messageSource;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
        verificationTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void signup() throws Exception {
        SignupForm signupForm = SignupForm.builder()
                .username("ffeffe")
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
    @DisplayName("이메일 발송")
    public void sendMail() throws Exception {
        VerificationRequest emailRequest = VerificationRequest.builder()
                .subject("jmcabc1216@gmail.com")
                .build();
        VerificationToken verificationToken = verificationProvider.sendVerificationCode(emailRequest.getSubject());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sendMail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(1, verificationTokenRepository.count());
    }

    @Test
    @DisplayName("이메일 발송 후 인증번호")
    public void verifyCode() throws Exception {
        VerificationToken verificationToken = verificationProvider.sendVerificationCode("jmcabc12167@gmail.com");
        verificationTokenRepository.save(verificationToken);
        VerificationRequest emailRequest = VerificationRequest.builder()
                .subject("jmcabc12167@gmail.com")
                .verificationCode(verificationToken.getVerificationCode())
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/mailVerify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(1, verificationTokenRepository.count());
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


        Login login = Login.builder()
                .username("ddd")
                .password("wjdals12")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
        //.andExpect(MockMvcResultMatchers.redirectedUrl());

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

        Login login = Login.builder()
                .username("username")
                .password("password")
                .build();


        //when login 이 성공했을때는 /로 redirect 된다. /의 권한은 USER 이다.
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
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

        Login login = Login.builder()
                .username("jmcabc@naver.com")
                .password(passwordEncoder.encode("wjdals12"))
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));



    }


}