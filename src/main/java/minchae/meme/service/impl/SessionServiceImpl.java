package minchae.meme.service.impl;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Session;
import minchae.meme.exception.Unauthorized;
import minchae.meme.repository.SessionRepository;
import minchae.meme.service.SessionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    @Override
    public Session getSessionByAccessToken(String accessToken) {
        return sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);
    }
}
