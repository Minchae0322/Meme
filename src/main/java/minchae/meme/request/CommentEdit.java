package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Post;

@Getter
@Setter
@NoArgsConstructor
public class CommentEdit {
    private Post post;
    private Long commentId;
    private String comment;
    private Long writerId;

    @Builder
    public CommentEdit(Post post, Long commentId, String comment, Long writerId) {
        this.post = post;
        this.commentId = commentId;
        this.comment = comment;
        this.writerId = writerId;
    }
}
