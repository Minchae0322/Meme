package minchae.meme.store;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileHandler {
    String getFullPath(String filename);

    UploadFile storeFile(MultipartFile multipartFile, Post post) throws IOException;

    boolean deleteFile(UploadFile uploadFile);

    boolean deleteFiles(List<UploadFile> uploadFiles);

    List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, Post post) throws IOException;

    List<InMemoryMultipartFile> extractFiles(List<UploadFile> uploadFiles) throws IOException;

    InMemoryMultipartFile extractFile(UploadFile uploadFile) throws IOException;

    String extractExt(String originalFilename);
}
