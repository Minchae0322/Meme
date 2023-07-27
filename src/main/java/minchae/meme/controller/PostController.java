package minchae.meme.controller;


import lombok.RequiredArgsConstructor;
import minchae.meme.request.PostCreate;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final Post_MemeServiceImpl postService;
    private final PostRepository postRepository;

    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId) {
       return postService.get(postId);
    }


    @PostMapping("/posts")
    public PostResponse writePost(@RequestBody PostCreate params) {
        postService.write(params);
        return PostResponse.builder()
                .title(params.getTitle())
                .content(params.getContent())
                .writerId(params.getWriterId())
                .build();
    }

    @DeleteMapping("/posts/{postId}")
    public Long deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        return postId;
    }
}
