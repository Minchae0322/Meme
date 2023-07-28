package minchae.meme.repository;

import minchae.meme.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom{
    public List<Comment> getCommentListWherePostId(Long postId);
}
