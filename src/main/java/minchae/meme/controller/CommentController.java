package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    private final PostRepository postRepository;


    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getCommentList(@PathVariable Long postId) {
        return commentService.getCommentList(postId);
    }

    @PostMapping("/posts/{postId}/comment")
    public void writeComment(@PathVariable Long postId, CommentCreate commentCreate) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 글입니다"));
        commentService.write(post, commentCreate);
    }

}
