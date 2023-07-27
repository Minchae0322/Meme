package minchae.meme.response;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    private Long writerId;

    @Builder
    public PostResponse(Long postId, String title, String content, int recommendation, int bad, int views, Long writerId) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.recommendation = recommendation;
        this.bad = bad;
        this.views = views;
        this.writerId = writerId;
    }
}
