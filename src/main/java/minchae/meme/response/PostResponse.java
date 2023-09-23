package minchae.meme.response;

import jakarta.persistence.Lob;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.entity.PostFunction;
import minchae.meme.entity.User;
import minchae.meme.entity.enumClass.PostType;
import minchae.meme.store.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private Long postId;

    private String title;

    @Lob
    private String content;

    private String author;
    private int recommendation;
    private int bad;
    private int views;

    private LocalDateTime createdTime;

    private PostFunction postFunction;

    private List<Comment> comments;

    private String youtubeUrl;

    private PostType postType;

    @Builder
    public PostResponse(Long postId, String title, String content, String author, int recommendation, int bad, int views, LocalDateTime createdTime, PostFunction postFunction, List<Comment> comments, String youtubeUrl, PostType postType) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.recommendation = recommendation;
        this.bad = bad;
        this.views = views;
        this.createdTime = createdTime;
        this.postFunction = postFunction;
        this.comments = comments;
        this.youtubeUrl = youtubeUrl;
        this.postType = postType;
    }

    @Transactional
    public PostResponse postToPostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor().getNickName();
        this.recommendation = post.getRecommendation();
        this.bad = post.getBad();
        this.views = post.getViews();
        this.postFunction = post.getPostFunction();
        this.comments = post.getComments();
        this.youtubeUrl = post.getYoutubeUrl();
        this.createdTime = post.getCreatedTime();
        this.postType = post.getPostType();
        return this;
    }

}
