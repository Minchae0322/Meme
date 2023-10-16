package minchae.meme.store;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile_post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class PostFileHandler extends FileHandlerImpl {

    public UploadFile_post storeFile(MultipartFile multipartFile) throws IOException {
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
                .build();
    }
}
