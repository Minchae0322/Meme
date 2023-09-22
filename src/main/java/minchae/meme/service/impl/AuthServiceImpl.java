package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.auth.VerificationProvider;
import minchae.meme.entity.VerificationToken;
import minchae.meme.repository.VerificationTokenRepository;

import minchae.meme.request.VerificationRequest;
import minchae.meme.service.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final VerificationTokenRepository verificationTokenRepository;

    private final VerificationProvider verificationProvider;

    @Override
    @Transactional
    public void sendVerificationCode(String subject) {
        VerificationToken verificationToken = verificationProvider.sendVerificationCode(subject);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public boolean isVerified(VerificationRequest verificationRequest) {
        return verificationProvider.verify(verificationRequest);
    }


}

