package minchae.meme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private Long postId;

    private String title;

    private String content;

    private int recommendation;

    private int bad;

    private int views;

    private User user;

    private List<Comment> comments;


    @Builder
    public PostResponse(Long postId, String title, String content, int recommendation, int bad, int views, User user, List<Comment> comments) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.recommendation = recommendation;
        this.bad = bad;
        this.views = views;
        this.user = user;
        this.comments = comments;
    }

    public PostResponse postToPostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.recommendation = post.getRecommendation();
        this.bad = post.getBad();
        this.views = post.getViews();
        this.user = post.getUser();
        this.comments = post.getComments();
        return this;
    }

}
