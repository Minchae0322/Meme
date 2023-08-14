package minchae.meme.service;

import minchae.meme.entity.Session;

public interface SessionService {
    Session getSessionByAccessToken(String accessToken);
}
