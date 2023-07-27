package minchae.meme.service;

import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;

public interface PostService {
    void write(PostCreate postCreate);
    PostResponse get(Long postId);

    void delete(Long postId);

    void update(Long postId, PostEdit postEdit);


}
