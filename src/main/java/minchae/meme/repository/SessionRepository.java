package minchae.meme.repository;

import minchae.meme.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> , SessionRepositoryCustom{

}
