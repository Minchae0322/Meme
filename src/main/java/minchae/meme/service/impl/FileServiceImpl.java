package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.UploadFile_post;
import minchae.meme.repository.UploadFileRepository;
import minchae.meme.response.FileResponse;
import minchae.meme.service.FileService;
import minchae.meme.store.FileHandler;
import minchae.meme.store.InMemoryMultipartFile;
import minchae.meme.store.PostFileHandler;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UploadFileRepository fileRepository;

    private final FileHandler fileHandler;

    private final PostFileHandler postFileHandler;

    @Override
    @Transactional
    public void write(UploadFile uploadFile) {
        fileRepository.save(uploadFile);
    }

    @Override
    @Transactional
    public void writeList(List<UploadFile_post> uploadFiles) {
        fileRepository.saveAll(uploadFiles);
    }

    @Override
    @Transactional
    public void saveFiles(List<MultipartFile> files, Post post) throws IOException {
        List<UploadFile_post> uploadFiles = fileHandler.storeFiles(files, post);
        if (uploadFiles != null) {
            writeList(uploadFiles);
        }
    }

    @Override
    public FileResponse getFiles(Long postId) throws IOException {
        return FileResponse.builder()
                .multipartFileList(fileHandler.extractFiles(getUploadFilesByPostId(postId)))
                .build();
    }

    @Override
    @Transactional
    public List<? extends UploadFile> getUploadFilesByPostId(Long postId) {
        return fileRepository.findUploadFilesByPostId(postId);
    }

    @Override
    public MultipartFile getFile(UploadFile uploadFile) throws IOException {
        return fileHandler.extractFile(uploadFile);
    }

    @Override
    public void saveFile(MultipartFile file, Post post) throws IOException {
        UploadFile_post uploadFile = fileHandler.storeFile(file, post);
        if (uploadFile != null) {
            write(uploadFile);
        }
    }

    @Override
    public boolean deleteFile(UploadFile uploadFile) {
        fileRepository.delete(uploadFile);
        return fileHandler.deleteFile(uploadFile);
    }

    @Override
    public boolean deleteFiles(List<? extends UploadFile> uploadFiles) {
        fileRepository.deleteAll(uploadFiles);
        return fileHandler.deleteFiles(uploadFiles);
    }
}
