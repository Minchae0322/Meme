package minchae.meme.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.QUpDown;
import minchae.meme.entity.UpDown;
import minchae.meme.repository.UpDownRepositoryCustom;

import java.util.List;

@RequiredArgsConstructor
public class UpDownRepositoryCustomImpl implements UpDownRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<UpDown> findByPostIdAndUserId(Long postId, Long userId) {
        return jpaQueryFactory.selectFrom(QUpDown.upDown)
                .where(QUpDown.upDown.post.postId.eq(postId))
                .where(QUpDown.upDown.user.id.eq(userId))
                .fetch();
    }
}
