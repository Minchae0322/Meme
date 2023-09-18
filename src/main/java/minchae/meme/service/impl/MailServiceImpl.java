package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.MailAuthentication;
import minchae.meme.repository.MailRepository;
import minchae.meme.request.EmailRequest;
import minchae.meme.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender; // JavaMailSender import 추가
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender; // JavaMailSender 필드 추가
    private final MailRepository mailRepository;

    @Override
    @Transactional
    public void sendMail(String email) {
        // 1. 메일 수신자 설정
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jmcabc1216@gmail.com");
        mailMessage.setTo(email); // 수신자 이메일 주소 설정
        mailMessage.setSubject("MEME 인증번호 입니다."); // 이메일 제목 설정
        String verificationCode = generateRandomVerificationCode();
        mailMessage.setText("인증번호: " + verificationCode); // 인증번호 이메일 내용 설정
        MailAuthentication mailAuthentication = MailAuthentication.builder()
                .verificationCode(verificationCode)
                .email(email)
                .build();
        write(mailAuthentication);
        // 2. 메일 발송
        javaMailSender.send(mailMessage);
    }

    @Override
    public void write(MailAuthentication mailAuthentication) {
        mailRepository.save(mailAuthentication);
    }

    @Override
    public boolean isVerifyEmailAndCode(EmailRequest emailRequest) {
        return mailRepository.findMailAuthenticationByEmailAndVerificationCode(emailRequest.getEmail(),
                emailRequest.getVerificationCode()).isPresent();
    }



    private String generateRandomVerificationCode() {
        int length = 6; // 인증번호 길이 설정 (6자리 예시)
        String characters = "0123456789"; // 인증번호로 사용할 문자열 설정
        StringBuilder verificationCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            verificationCode.append(characters.charAt(index));
        }

        return verificationCode.toString();
    }
}

