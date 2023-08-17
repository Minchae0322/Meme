package minchae.meme.controller;


import lombok.RequiredArgsConstructor;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final Post_MemeServiceImpl postService;
    private final PostRepository postRepository;

    @GetMapping("/board/posts/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

    @PostMapping("/board/user/writePost")
    public PostResponse writePost(@RequestBody PostCreate params) {
        postService.write(params);
        return PostResponse.builder()
                .title(params.getTitle())
                .content(params.getContent())
                .user(params.getUser())
                .build();
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

    @GetMapping("/hotList")
    public List<PostResponse> unsetHotPost(Page page) {
        return postService.getHotListWherePage(page);
    }
}
