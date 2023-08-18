package minchae.meme.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileCreate {
    private List<MultipartFile> imageFiles;    // 첨부 이미지
}
