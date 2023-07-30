package minchae.meme.repository;

import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;

import java.util.List;

public interface CommentRepositoryCustom{
    List<Comment> getCommentListWherePostId(Long postId);


}
