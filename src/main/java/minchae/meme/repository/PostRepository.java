package minchae.meme.repository;

import minchae.meme.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom{
}
