package minchae.meme.request;

import lombok.*;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentEdit {
    private Post post;
    private String comment;
    private User author;

    @Builder
    public CommentEdit(Post post, String comment, User author) {
        this.post = post;
        this.comment = comment;
        this.author = author;
    }
}
