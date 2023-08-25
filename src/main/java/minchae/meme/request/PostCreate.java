package minchae.meme.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class PostCreate implements Serializable {

    private String title;

    private String content;

    private User user;

    private String youtubeUrl;


    @Builder
    public PostCreate(String title, String content, User user, String youtubeUrl) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.youtubeUrl = youtubeUrl;
    }


}
