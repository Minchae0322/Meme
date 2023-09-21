package minchae.meme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;
import minchae.meme.entity.CommentFunction;
import minchae.meme.entity.Post;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {


    private Long commentId;
    private String comment;
    private String author;

    private LocalDateTime createdTime;

    private CommentFunction commentFunction;


    @Builder
    public CommentResponse(Long commentId, String comment, String author, LocalDateTime createdTime, CommentFunction commentFunction) {
        this.commentId = commentId;
        this.comment = comment;
        this.author = author;
        this.createdTime = createdTime;
        this.commentFunction = commentFunction;
    }

    public CommentResponse commentToCommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.comment = comment.getComment();
        this.author = comment.getUser().getNickName();
        this.createdTime = comment.getCreatedTime();
        this.commentFunction = comment.getCommentFunction();
        return this;
    }
}
