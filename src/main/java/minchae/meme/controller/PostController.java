package minchae.meme.controller;


import lombok.RequiredArgsConstructor;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.FileService;
import minchae.meme.service.impl.PostServiceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    private final JwtTokenProvider jwtTokenProvider;

    private final FileService fileService;
    private final PostRepository postRepository;

    private final UserRepository userRepository;



    @GetMapping("/board/posts/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/board/posts/{postId}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable("postId") Long postId) throws IOException {
        List<? extends MultipartFile> imageFiles = fileService.getFiles(postId).getMultipartFileList();

        if (imageFiles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (MultipartFile imageFile : imageFiles) {
            // 각 이미지 파일의 바이트 데이터를 합칩니다.
            byte[] imageData = imageFile.getBytes();
            baos.write(imageData);
        }

        byte[] combinedImageData = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 이미지인 경우에는 해당 MIME 타입을 설정합니다.
        headers.setContentLength(combinedImageData.length);

        return new ResponseEntity<>(combinedImageData, headers, HttpStatus.OK);
    }




    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANAGER')")
    @PostMapping(value = "/board/user/writePost", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void writePost(Authentication authentication, @RequestPart("post") PostCreate params, @RequestPart(value = "imageFile", required = false) List<MultipartFile> multipartFiles) throws IOException {
        User user = (User) authentication.getPrincipal();
        params.setUser(user);
        Post post = postService.write(params);
        if (multipartFiles != null) {
            fileService.saveFiles(multipartFiles, post);
        }
        postService.setHotPost(post.getPostId());
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER') || (hasPermission(#postId,'POST','DELETE') && hasAuthority('USER'))")
    @DeleteMapping("/board/user/{postId}")
    public Long deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        return postId;
    }
    @PreAuthorize("hasPermission(#postId,'POST','UPDATE') && hasAuthority('USER')")
    @PatchMapping("/board/user/{postId}")
    public PostResponse updatePost(@PathVariable("postId") Long postId, @RequestBody PostEdit postEdit) {
         return postService.update(postId, postEdit);
    }


    @GetMapping("/board/posts/list")
    public List<PostResponse> getPostListWherePage(@PageableDefault Page page) {
        return postService.getListWherePage(page);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PostMapping("/admin/setHot/{postId}")
    public void setHotPost(@PathVariable("postId") Long postId) {
        postService.setHotPost(postId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PostMapping("/admin/unsetHot/{postId}")
    public void unsetHotPost(@PathVariable Long postId) {
        postService.unsetHotPost(postId);
    }

    @GetMapping("/board/posts/hotList")
    public List<PostResponse> getHotPostList(@PageableDefault Page page) {
        return postService.getHotListWherePage(page);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER', 'USER')")
    @PostMapping("/board/user/{postId}/up")
    public int upRecommendation(@PathVariable("postId") Long postId, Authentication authentication) {
        return postService.upRecommendation(postId, (User) authentication.getPrincipal());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER', 'USER')")
    @PostMapping("/board/user/{postId}/bad")
    public int upBad(@PathVariable("postId") Long postId, Authentication authentication) {
        return postService.upBad(postId, (User) authentication.getPrincipal());
    }
}
