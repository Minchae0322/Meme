package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minchae.meme.entity.User;

@Getter
@Setter
public class PostEdit {

    private String title;

    private String content;

    private User user;

    @Builder
    public PostEdit(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
