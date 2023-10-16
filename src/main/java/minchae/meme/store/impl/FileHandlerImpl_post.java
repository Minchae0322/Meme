package minchae.meme.store.impl;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.UploadFile_post;
import minchae.meme.store.FileHandler;
import minchae.meme.store.InMemoryMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Component
public class FileHandlerImpl_post implements FileHandler {

    String rootPath = System.getProperty("user.dir");

    // 프로젝트 루트 경로에 있는 files 디렉토리
    private final String fileDir = rootPath + "/files/";

    @Override
    public String getFullPath(String filename) { return fileDir + filename; }

    @Override
    public UploadFile_post storeFile(MultipartFile multipartFile, Object objectBy) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로

        String storeFilename = UUID.randomUUID() + "." + extractExt(originalFilename);
        System.out.println(getFullPath(storeFilename));
        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장
        multipartFile.transferTo(Path.of(getFullPath(storeFilename)));

        return UploadFile_post.builder()
                .orgFileName(originalFilename)
                .storeFileName(storeFilename)
                .post((Post) objectBy)
                .build();
    }

    // 파일이 여러개 들어왔을 때 처리해주는 부분
    @Override
    public List<UploadFile_post> storeFiles(List<MultipartFile> multipartFiles, Object objectBy) throws IOException {
        List<UploadFile_post> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, objectBy));
            }
        }
        return storeFileResult;
    }

    @Override
    public List<InMemoryMultipartFile> extractFiles(List<? extends UploadFile> uploadFiles) throws IOException {
        List<InMemoryMultipartFile> list = new ArrayList<>();
        for (UploadFile file : uploadFiles) {
            list.add(extractFile(file));

        }
        return list;
    }

    @Override
    public boolean deleteFile(UploadFile uploadFile) {
        String storeFileName = uploadFile.getStoreFileName();
        String filePath = getFullPath(storeFileName);
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                return true; // File deleted successfully
            } else {
                return false; // Failed to delete the file
            }
        } else {
            return true; // File doesn't exist, treat as a successful deletion
        }
    }

    @Override
    public boolean deleteFiles(List<? extends UploadFile> uploadFiles) {
        for (UploadFile file : uploadFiles) {
            if (!deleteFile(file)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public InMemoryMultipartFile extractFile(UploadFile uploadFile) throws IOException {
        File inMemoryFile = new File(getFullPath(uploadFile.getStoreFileName()));
        byte[] fileContent = FileCopyUtils.copyToByteArray(inMemoryFile);
        return new InMemoryMultipartFile(uploadFile.getOrgFileName(), fileContent);
    }

    // 확장자 추출
    @Override
    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
