package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.UploadFile_post;
import minchae.meme.response.FileResponse;
import minchae.meme.store.InMemoryMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    void write(UploadFile uploadFile);

    void writeList(List<? extends UploadFile> uploadFiles);

    void saveFiles(List<MultipartFile> files, Object post) throws IOException;

    FileResponse getFiles(Long postId) throws IOException;

    List<? extends UploadFile> getUploadFilesByPostId(Long postId);

    MultipartFile getFile(UploadFile uploadFile) throws IOException;

    void saveFile(MultipartFile file, Object post) throws IOException;

    boolean deleteFile(UploadFile uploadFile);

    boolean deleteFiles(List<? extends UploadFile> uploadFiles);
}
