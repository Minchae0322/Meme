package minchae.meme.service.impl;

import lombok.RequiredArgsConstructor;
import minchae.meme.repository.CommentRepository;
import minchae.meme.request.CommentCreate;
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
        return null;
    }

    @Override
    public void delete(Long commentId) {

    }

    @Override
    public CommentResponse write(CommentCreate commentCreate) {
        return null;
    }

    @Override
    public CommentResponse update(Long commentId, CommentCreate commentCreate) {
        return null;
    }
}
