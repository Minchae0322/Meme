package minchae.meme.service;

import minchae.meme.request.CommentCreate;
import minchae.meme.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentList(Long postId);

    CommentResponse getComment(Long commentId);

    void delete(Long commentId);

    CommentResponse write(CommentCreate commentCreate);

    CommentResponse update(Long commentId, CommentCreate commentCreate);

}
