package minchae.meme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {


    private Long commentId;
    private Post post;
    private String comment;
    private User user;

    private int recommendation = 0;

    private int bad = 0;

    @Builder
    public CommentResponse(Long commentId, Post post, String comment, User writerId, int recommendation, int bad) {
        this.commentId = commentId;
        this.post = post;
        this.comment = comment;
        this.user = writerId;
        this.recommendation = recommendation;
        this.bad = bad;
    }

    public CommentResponse commentToCommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.post = comment.getPost();
        this.comment = comment.getComment();
        this.user = comment.getUser();
        this.recommendation = comment.getRecommendation();
        this.bad = comment.getBad();
        return this;
    }
}
