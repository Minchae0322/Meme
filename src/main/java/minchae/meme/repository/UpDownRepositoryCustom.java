package minchae.meme.repository;

import minchae.meme.entity.UpDown;

import java.util.List;

public interface UpDownRepositoryCustom {
    List<UpDown> findByPostIdAndUserId(Long postId, Long userId);


}
