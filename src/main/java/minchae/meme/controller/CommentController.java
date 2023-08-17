package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.request.CommentVo;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    private final PostRepository postRepository;


    @GetMapping("/board/posts/{postId}/comments")
    public List<CommentResponse> getCommentList(@PathVariable Long postId) {
        return commentService.getCommentList(postId);
    }

    @GetMapping("/board/posts/comment/{commentId}")
    public CommentResponse getComment(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    @PostMapping("/board/posts/{postId}/comments")
    public void writeComment(@PathVariable Long postId, @RequestBody CommentCreate commentCreate) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 글입니다"));
        commentService.write(post, commentCreate);
    }

    @PostMapping("/board/posts/{commentId}/up")
    public CommentResponse upRecommendation(@PathVariable Long commentId) {
        return commentService.upRecommendation(commentId);
    }

    @PostMapping("/board/posts/{commentId}/bad")
    public CommentResponse upBad(@PathVariable Long commentId) {
        return commentService.upBad(commentId);
    }


    @PatchMapping("/board/posts/{postId}/comments/{commentId}")
    public CommentResponse writeComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentEdit commentEdit) {
        return commentService.update(commentId, commentEdit);
    }

    @DeleteMapping("/board/posts/{postId}/comments/{commentId}")
    public void getCommentList(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.delete(postId, commentId);
    }


    @PostMapping("/board/posts/{postId}/commentList")
    public void writeCommentList(@PathVariable Long postId, @RequestBody CommentVo commentVo) {
        commentService.writeCommentList(commentVo.getCommentCreateList());
    }

    @DeleteMapping("/board/posts/{postId}/commentList")
    public void deleteCommentList(@PathVariable Long postId) {
        commentService.deleteCommentList(postId);
    }
}
