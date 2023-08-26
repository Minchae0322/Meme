package minchae.meme.store;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface Store {
    String getFullPath(String filename);

    UploadFile storeFile(MultipartFile multipartFile, Post post) throws IOException;

    List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, Post post) throws IOException;

    String extractExt(String originalFilename);
}
