package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.exception.CommentNotFound;
import minchae.meme.exception.PostNotFound;
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
                .orElseThrow(CommentNotFound::new);
        return CommentResponse.builder().build().commentToCommentResponse(comment);
    }

    @Override
    @Transactional
    public void delete(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        post.getComments().remove(comment);
        commentRepository.delete(comment);
    }

    @Override
    public void write(Post post, CommentCreate commentCreate) {
        Comment comment = Comment.builder()
                .post(post)
                .comment(commentCreate.getComment())
                .user(commentCreate.getUser())
                .build();
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public CommentResponse update(Long commentId, CommentEdit commentEdit) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);
        comment.update(commentEdit);
        return CommentResponse.builder().build().commentToCommentResponse(comment);
    }

    @Override
    public void deleteCommentList(Long postId) {
        commentRepository.deleteCommentListWherePostId(postId);
    }

    @Override
    @Transactional
    public CommentResponse upRecommendation(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);
        comment.setRecommendation(comment.getRecommendation() + 1);
        return CommentResponse.builder().build().commentToCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse upBad(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);
        comment.setBad(comment.getBad() + 1);
        return CommentResponse.builder().build().commentToCommentResponse(comment);
    }

    @Override
    public void writeCommentList(List<CommentCreate> commentCreateList) {
        List<Comment> commentList = commentCreateList.stream().map(commentCreate -> Comment.builder()
                        .post(commentCreate.getPost())
                        .comment(commentCreate.getComment())
                        .bad(commentCreate.getBad())
                        .recommendation(commentCreate.getRecommendation())
                        .user(commentCreate.getUser())
                        .build())
                .collect(Collectors.toList());
        commentRepository.saveAll(commentList);
    }
}
