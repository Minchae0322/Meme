package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.repository.CommentRepository;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;


    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getCommentList(@PathVariable Long postId) {
        return commentService.getCommentList(postId);
    }

}
