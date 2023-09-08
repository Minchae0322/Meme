package minchae.meme.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.QPost;
import minchae.meme.entity.QUploadFile;
import minchae.meme.entity.UploadFile;
import minchae.meme.repository.UploadFileRepository;
import minchae.meme.repository.UploadFileRepositoryCustom;

import java.util.List;

@RequiredArgsConstructor
public class UploadFileRepositoryCustomImpl implements UploadFileRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<UploadFile> findUploadFilesByPostId(Long postId) {
        return jpaQueryFactory.selectFrom(QUploadFile.uploadFile)
                .where(QUploadFile.uploadFile.post.postId.eq(postId))
                .fetch();
    }
}
