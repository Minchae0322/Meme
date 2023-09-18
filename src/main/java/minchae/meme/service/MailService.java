package minchae.meme.service;

import minchae.meme.entity.MailAuthentication;
import minchae.meme.request.EmailRequest;

public interface MailService {
    void sendMail(String email);

    void write(MailAuthentication mailAuthentication);

    boolean isVerifyEmailAndCode(EmailRequest emailRequest);

}
