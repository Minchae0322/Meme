package minchae.meme.auth.provider;

import lombok.RequiredArgsConstructor;
import minchae.meme.auth.VerificationProvider;
import minchae.meme.entity.VerificationToken;
import minchae.meme.repository.VerificationTokenRepository;
import minchae.meme.request.VerificationRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailVerificationProvider implements VerificationProvider {

    private final JavaMailSender javaMailSender;

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken sendVerificationCode(String subject) {
        String verificationCode = generateRandomVerificationCode();
        SimpleMailMessage simpleMailMessage = makeSimpleMailMessage(subject, verificationCode);
        VerificationToken verificationToken = VerificationToken.builder()
                .verificationCode(verificationCode)
                .verificationSubject(subject)
                .build();
        // 2. 메일 발송
        javaMailSender.send(simpleMailMessage);
        return verificationToken;
    }

    @Override
    public boolean verify(VerificationRequest verificationRequest) {
        return verificationTokenRepository.findVerificationTokenByVerificationSubjectAndVerificationCode(verificationRequest.getSubject(),
                verificationRequest.getVerificationCode()).isPresent();
    }

    private SimpleMailMessage makeSimpleMailMessage(String email, String verificationCode) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jmcabc1216@gmail.com");
        mailMessage.setTo(email); // 수신자 이메일 주소 설정
        mailMessage.setSubject("MEME 인증번호 입니다."); // 이메일 제목 설정
        mailMessage.setText("인증번호: " + verificationCode); // 인증번호 이메일 내용 설정
        return mailMessage;
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
