package minchae.meme.repository;

import minchae.meme.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> , RecommendationRepositoryCustom {
}
