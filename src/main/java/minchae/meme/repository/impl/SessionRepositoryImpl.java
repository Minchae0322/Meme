package minchae.meme.repository.impl;

import minchae.meme.entity.Session;
import minchae.meme.repository.SessionRepositoryCustom;

import java.util.Optional;

public class SessionRepositoryImpl implements SessionRepositoryCustom {


    @Override
    public Optional<Session> findByAccessToken(String accessToken) {
        return null;
    }
}
