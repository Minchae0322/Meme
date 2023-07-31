package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentResponse> getCommentList(Long postId) {
        return commentRepository.getCommentListWherePostId(postId)
                .stream().map(comment -> CommentResponse.builder().build().commentToCommentResponse(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 댓글입니다"));
        return CommentResponse.builder().build().commentToCommentResponse(comment);
    }

    @Override
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 댓글입니다"));
        commentRepository.delete(comment);
    }

    @Override
    public void write(Post post, CommentCreate commentCreate) {
        Comment comment = Comment.builder()
                .post(post)
                .comment(commentCreate.getComment())
                .writerId(commentCreate.getWriterId())
                .build();
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public CommentResponse update(Long commentId, CommentEdit commentEdit) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 댓글입니다"));
        comment.update(commentEdit);
        return CommentResponse.builder().build().commentToCommentResponse(comment);
    }

    @Override
    public void deleteCommentList(Long postId) {
        commentRepository.deleteCommentListWherePostId(postId);
    }
}
