package minchae.meme.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class FileResponse {
    private List<? extends MultipartFile> multipartFileList;

    @Builder
    public FileResponse(List<? extends MultipartFile> multipartFileList) {
        this.multipartFileList = multipartFileList;
    }
}
