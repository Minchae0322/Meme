package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentList(Long postId);

    CommentResponse getComment(Long commentId);

    void delete(Long postId, Long commentId);

    void write(Long postId, CommentCreate commentCreate);

    CommentResponse update(Long commentId, CommentEdit commentEdit);

    void deleteCommentList(Long postId);

  /*  CommentResponse upRecommendation(Long commentId);

    CommentResponse upBad(Long commentId);*/

}
