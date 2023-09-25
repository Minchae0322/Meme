package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.request.CommentVo;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANAGER')")
    @PostMapping("/board/user/{postId}/comments")
    public void writeComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentCreate commentCreate) {
        User user = (User) authentication.getPrincipal();
        commentCreate.setUser(user);
        commentService.write(postId, commentCreate);
    }

    /*@PostMapping("/board/posts/{commentId}/up")
    public CommentResponse upRecommendation(@PathVariable Long commentId) {
        return commentService.upRecommendation(commentId);
    }*/

    /*@PostMapping("/board/posts/{commentId}/bad")
    public CommentResponse upBad(@PathVariable Long commentId) {
        return commentService.upBad(commentId);
    }*/

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER') || (hasPermission(#commentId,'COMMENT','UPDATE') && hasAuthority('USER'))")
    @PatchMapping("/board/user/comments/{commentId}")
    public CommentResponse updateComment(@PathVariable Long commentId, @RequestBody CommentEdit commentEdit) {
        return commentService.update(commentId, commentEdit);
    }



    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER') || (hasPermission(#commentId,'COMMENT','DELETE') && hasAuthority('USER'))")
    @DeleteMapping("/board/user/{postId}/{commentId}/delete")
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.delete(postId, commentId);
    }


    @DeleteMapping("/board/posts/{postId}/commentList")
    public void deleteCommentList(@PathVariable Long postId) {
        commentService.deleteCommentList(postId);
    }
}
