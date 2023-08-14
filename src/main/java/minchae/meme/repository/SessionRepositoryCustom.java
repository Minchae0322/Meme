package minchae.meme.repository;

import minchae.meme.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepositoryCustom {
    List<Session> findByAccessToken(String accessToken);
}
