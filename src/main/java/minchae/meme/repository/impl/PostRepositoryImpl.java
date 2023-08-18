package minchae.meme.repository.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.QPost;
import minchae.meme.entity.Recommendation;
import minchae.meme.repository.PostRepositoryCustom;
import minchae.meme.request.Page;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPostList(Page page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(page.getSize())
                .orderBy(QPost.post.createdTime.desc())
                .offset((long) (page.getPage() - 1) * page.getSize())
                .fetch();
    }

    @Override
    public List<Post> getHotList(Page page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .where(QPost.post.isHot)
                .orderBy(QPost.post.createdTime.desc())
                .limit(page.getSize())
                .offset((long) (page.getPage() - 1) * page.getSize())
                .fetch();
    }




}
