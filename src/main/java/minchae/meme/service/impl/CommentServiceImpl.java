package minchae.meme.service.impl;

import minchae.meme.request.CommentCreate;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.CommentService;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    @Override
    public List<CommentResponse> getCommentList(Long postId) {
        return null;
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
