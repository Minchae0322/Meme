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

    private String youtubeUrl;

    private User user;

    @Builder
    public PostEdit(String title, String content, String youtubeUrl, User user) {
        this.title = title;
        this.content = content;
        this.youtubeUrl = youtubeUrl;
        this.user = user;
    }
}
