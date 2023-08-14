package minchae.meme.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.QSession;
import minchae.meme.entity.Session;
import minchae.meme.repository.SessionRepositoryCustom;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Session> findByAccessToken(String accessToken) {
        return jpaQueryFactory.selectFrom(QSession.session)
                .limit(1)
                .where(QSession.session.accessToken.eq(accessToken))
                .fetch();
    }
}
