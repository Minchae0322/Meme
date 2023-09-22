package minchae.meme.auth;

import minchae.meme.entity.VerificationToken;
import minchae.meme.request.VerificationRequest;

public interface VerificationProvider {
    VerificationToken sendVerificationCode(String subject);

    boolean verify(VerificationRequest verificationRequest);
}
