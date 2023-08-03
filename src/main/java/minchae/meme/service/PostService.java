package minchae.meme.service;

import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;

import java.util.List;

public interface PostService {
    void write(PostCreate postCreate);
    PostResponse get(Long postId);

    void delete(Long postId);

    PostResponse update(Long postId, PostEdit postEdit);

    List<PostResponse> getListWherePage(Page page);


}
