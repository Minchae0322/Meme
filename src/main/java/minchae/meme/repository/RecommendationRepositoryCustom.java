package minchae.meme.repository;

import minchae.meme.entity.Recommendation;

import java.util.List;

public interface RecommendationRepositoryCustom {
    List<Recommendation> findByPostIdAndUserId(Long postId, Long userId);
}
