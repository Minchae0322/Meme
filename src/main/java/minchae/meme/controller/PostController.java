package minchae.meme.controller;


import lombok.RequiredArgsConstructor;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.User;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.FileResponse;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.FileService;
import minchae.meme.service.impl.PostServiceImpl;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
    public ResponseEntity<List<String>> getPostImage(@PathVariable("postId") Long postId) throws IOException {
        List<? extends MultipartFile> imageFiles = fileService.getFiles(postId).getMultipartFileList();
        if (imageFiles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<String> imageBase64List = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            byte[] imageData = imageFile.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(imageData);
            imageBase64List.add(imageBase64);
        }

        return ResponseEntity.ok(imageBase64List);
    }


    @PostMapping(value = "/board/user/writePost", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void writePost(@RequestPart("post") PostCreate params, @RequestPart(value = "imageFile", required = false) List<MultipartFile> multipartFiles) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        params.setUser(user);
        Post post = postService.write(params);
        if (multipartFiles != null) {
            fileService.saveFiles(multipartFiles, post);
        }
    }


    @DeleteMapping("/board/user/{postId}")
    public Long deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        return postId;
    }

    @PatchMapping("/board/user/{postId}")
    public PostResponse updatePost(@PathVariable("postId") Long postId, @RequestBody PostEdit postEdit) {
         return postService.update(postId, postEdit);
    }


    @GetMapping("/board/posts/list")
    public List<PostResponse> getPostListWherePage(@PageableDefault Page page) {
        return postService.getListWherePage(page);
    }

    @PostMapping("/admin/setHot/{postId}")
    public void setHotPost(@PathVariable("postId") Long postId) {
        postService.setHotPost(postId);
    }

    @PostMapping("/admin/unsetHot/{postId}")
    public void unsetHotPost(@PathVariable Long postId) {
        postService.unsetHotPost(postId);
    }

    @GetMapping("/board/hotList")
    public List<PostResponse> unsetHotPost(Page page) {
        return postService.getHotListWherePage(page);
    }

    @PostMapping("/board/user/{postId}/up")
    public int upRecommendation(@PathVariable("postId") Long postId, @AuthenticationPrincipal User user) {
        Post post = postRepository.findById(postId).orElseThrow();
        return postService.upRecommendation(post, user);
    }
}
