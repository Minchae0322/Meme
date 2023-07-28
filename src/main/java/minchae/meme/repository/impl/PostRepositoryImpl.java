package minchae.meme.repository.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.QPost;
import minchae.meme.repository.PostRepositoryCustom;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPostList(int page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(5)
                .offset((page - 1) * 5L)
                .fetch();
    }
}
