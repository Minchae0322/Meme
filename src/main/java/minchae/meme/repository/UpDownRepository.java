package minchae.meme.repository;

import minchae.meme.entity.UpDown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpDownRepository extends JpaRepository<UpDown, Long> , UpDownRepositoryCustom {
}
