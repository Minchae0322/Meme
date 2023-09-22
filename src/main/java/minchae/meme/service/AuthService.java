package minchae.meme.service;


import minchae.meme.request.VerificationRequest;

public interface AuthService {
    void sendVerificationCode(String subject);


    boolean isVerified(VerificationRequest verificationRequest);

}
