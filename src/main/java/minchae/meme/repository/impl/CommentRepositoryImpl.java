package minchae.meme.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.QComment;
import minchae.meme.entity.QPost;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Comment> getCommentListWherePostId(Long postId) {
        return jpaQueryFactory.select(QComment.comment1)
                .from(QComment.comment1)
                .join(QPost.post)
                .on(QPost.post.eq(QComment.comment1.post))
                //todo where은 안되는이유
                .fetch();
    }
}
