package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
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

    private final PostRepository postRepository;
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
    @Transactional
    public void delete(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 댓글입니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));
        post.getComments().remove(comment);
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

    @Override
    @Transactional
    public int upRecommendation(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        comment.setRecommendation(comment.getRecommendation() + 1);
        return comment.getRecommendation() + 1;
    }

    @Override
    @Transactional
    public int upBad(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        comment.setBad(comment.getBad() + 1);
        return comment.getBad() + 1;
    }
}
