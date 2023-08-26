package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    void write(UploadFile uploadFile);

    void writeList(List<UploadFile> uploadFiles);

    List<UploadFile> saveFiles(List<MultipartFile> files, Post post) throws IOException;


    UploadFile saveFile(MultipartFile file, Post post) throws IOException;
}
