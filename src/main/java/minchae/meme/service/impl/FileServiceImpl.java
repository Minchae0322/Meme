package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.repository.UploadFileRepository;
import minchae.meme.service.FileService;
import minchae.meme.store.FileStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UploadFileRepository fileRepository;

    private final FileStore fileStore;
    @Override
    @Transactional
    public void write(UploadFile uploadFile) {
        fileRepository.save(uploadFile);
    }

    @Override
    @Transactional
    public void writeList(List<UploadFile> uploadFiles) {
        fileRepository.saveAll(uploadFiles);
    }

    @Override
    @Transactional
    public List<UploadFile> saveFiles(List<MultipartFile> files, Post post) throws IOException {
        return fileStore.storeFiles(files, post);
    }
}
