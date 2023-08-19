package minchae.meme.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Embeddable
@RequiredArgsConstructor
@Getter
@Setter
public class PostFunction {

    private boolean isHot;


    @Builder
    public PostFunction(boolean isHot) {
        this.isHot = isHot;
    }
}
