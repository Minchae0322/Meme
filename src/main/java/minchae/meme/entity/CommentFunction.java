package minchae.meme.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class CommentFunction {

    private boolean isHot = false;

    @Builder
    public CommentFunction(boolean isHot) {
        this.isHot = isHot;
    }
}
