package minchae.meme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImageResponse {
    List<byte[]> imageData = new ArrayList<>();

    @Builder
    public ImageResponse(List<byte[]> imageData) {
        this.imageData = imageData;
    }
}
