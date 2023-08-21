package minchae.meme.controller;


import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.impl.PostServiceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;
    private final PostRepository postRepository;

    @GetMapping("/board/posts/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

    @PostMapping(value = "/board/user/writePost")
    public void writePost(@RequestBody PostCreate params) throws IOException {
        postService.write(params);
    }

    /*@ResponseBody
    @GetMapping("/images/{filename}")
    public List<Resource> showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }*/

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
