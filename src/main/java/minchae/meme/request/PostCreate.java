package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostCreate {

    private Long postId;

    private String title;

    private String content;

    private User user;

    private List<MultipartFile> imageFiles;    // 첨부 이미지

    private String youtubeUrl;

    @Builder
    public PostCreate(Long postId, String title, String content, User user, List<MultipartFile> imageFiles, String youtubeUrl) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.user = user;
        this.imageFiles = imageFiles;
        this.youtubeUrl = youtubeUrl;
    }
}
