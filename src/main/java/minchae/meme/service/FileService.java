package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.response.FileResponse;
import minchae.meme.store.InMemoryMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    void write(UploadFile uploadFile);

    void writeList(List<UploadFile> uploadFiles);

    void saveFiles(List<MultipartFile> files, Post post) throws IOException;

    FileResponse getFiles(Long postId) throws IOException;

    List<UploadFile> getUploadFilesByPostId(Long postId);

    MultipartFile getFile(UploadFile uploadFile) throws IOException;

    void saveFile(MultipartFile file, Post post) throws IOException;

    boolean deleteFile(UploadFile uploadFile);

    boolean deleteFiles(List<UploadFile> uploadFiles);
}
