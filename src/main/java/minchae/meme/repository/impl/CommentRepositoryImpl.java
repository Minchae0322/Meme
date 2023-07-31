package minchae.meme.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
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
        return jpaQueryFactory.selectFrom(QComment.comment1)
                .where(QComment.comment1.post.postId.eq(postId))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteCommentListWherePostId(Long postId) {
        jpaQueryFactory.delete(QComment.comment1)
                .where(QComment.comment1.post.postId.eq(postId))
                .execute();
    }

}
