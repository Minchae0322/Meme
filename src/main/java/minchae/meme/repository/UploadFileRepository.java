package minchae.meme.repository;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>, UploadFileRepositoryCustom {
}
