package minchae.meme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;
import minchae.meme.entity.CommentFunction;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {


    private Long commentId;
    private Post post;
    private String comment;
    private String author;

    private CommentFunction commentFunction;


    @Builder
    public CommentResponse(Long commentId, Post post, String comment, String author, CommentFunction commentFunction) {
        this.commentId = commentId;
        this.post = post;
        this.comment = comment;
        this.author = author;
        this.commentFunction = commentFunction;
    }

    public CommentResponse commentToCommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.post = comment.getPost();
        this.comment = comment.getComment();
        this.author = comment.getUser().getNickName();
        this.commentFunction = comment.getCommentFunction();
        return this;
    }
}
