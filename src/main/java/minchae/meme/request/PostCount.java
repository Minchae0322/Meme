package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCount {
    private String postType;

    @Builder
    public PostCount(String postType) {
        this.postType = postType;
    }
}
