package minchae.meme.repository;

import minchae.meme.entity.UploadFile;

import java.util.List;

public interface UploadFileRepositoryCustom {
    List<? extends UploadFile> findUploadFilesByPostId(Long postId);
}
