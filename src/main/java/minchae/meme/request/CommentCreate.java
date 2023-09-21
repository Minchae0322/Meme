package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreate {
    private String comment;
    private User user;

    @Builder
    public CommentCreate(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}
