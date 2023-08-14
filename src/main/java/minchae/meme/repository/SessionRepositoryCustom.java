package minchae.meme.repository;

import minchae.meme.entity.Session;

import java.util.Optional;

public interface SessionRepositoryCustom {
    Optional<Session> findByAccessToken(String accessToken);
}
