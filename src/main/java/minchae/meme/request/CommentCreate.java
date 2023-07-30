package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Post;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreate {
    private Post post;
    private String comment;
    private Long writerId;

    private int recommendation = 0;

    private int bad = 0;

    @Builder
    public CommentCreate(Post post, String comment, Long writerId, int recommendation, int bad) {
        this.post = post;
        this.comment = comment;
        this.writerId = writerId;
        this.recommendation = recommendation;
        this.bad = bad;
    }
}
