package minchae.meme.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.QRecommendation;
import minchae.meme.entity.Recommendation;
import minchae.meme.repository.RecommendationRepositoryCustom;

import java.util.List;

@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Recommendation> findByPostIdAndUserId(Long postId, Long userId) {
        return jpaQueryFactory.selectFrom(QRecommendation.recommendation)
                .where(QRecommendation.recommendation.post.postId.eq(postId))
                .where(QRecommendation.recommendation.user.id.eq(userId))
                .fetch();
    }
}
