package minchae.meme.controller;


import lombok.RequiredArgsConstructor;
import minchae.meme.entity.PostCreate;
import minchae.meme.entity.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId) {
       return postService.getPost_Meme(postId);
    }

    @PostMapping("/posts")
    public PostResponse writePost(@RequestBody PostCreate params) {
        postService.writePost_Meme(params);
        return PostResponse.builder()
                .title(params.getTitle())
                .content(params.getContent())
                .writerId(params.getWriterId())
                .build();
    }
}
