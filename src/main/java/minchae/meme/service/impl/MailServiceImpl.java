package minchae.meme.service.impl;

import lombok.RequiredArgsConstructor;
import minchae.meme.service.MailService;
import org.springframework.mail.SimpleMailMessage;

@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    SimpleMailMessage simpleMailMessage;
    @Override
    public void sendMail() {

    }
}
