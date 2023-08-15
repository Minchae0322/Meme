package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.User;

@NoArgsConstructor
@Getter
@Setter
public class PostCreate {

    private Long postId;

    private String title;

    private String content;

    private int recommendation;

    private int bad;

    private int views;

    private User user;

    @Builder
    public PostCreate(Long postId, String title, String content, int recommendation, int bad, int views, User user) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.recommendation = recommendation;
        this.bad = bad;
        this.views = views;
        this.user = user;
    }
}
