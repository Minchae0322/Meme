package minchae.meme.store;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.UploadFile_post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileHandler{
    String getFullPath(String filename);

    UploadFile storeFile(MultipartFile multipartFile, Object objectBy) throws IOException;

    boolean deleteFile(UploadFile uploadFile);

    boolean deleteFiles(List<? extends UploadFile> uploadFiles);

    List<? extends UploadFile> storeFiles(List<MultipartFile> multipartFiles, Object objectBy) throws IOException;

    List<InMemoryMultipartFile> extractFiles(List<? extends UploadFile> uploadFiles) throws IOException;

    InMemoryMultipartFile extractFile(UploadFile uploadFile) throws IOException;

    String extractExt(String originalFilename);
}
