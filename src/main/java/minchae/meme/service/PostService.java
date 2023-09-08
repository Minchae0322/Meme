package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.User;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post write(PostCreate postCreate);
    PostResponse get(Long postId);

    void delete(Long postId);

    PostResponse update(Long postId, PostEdit postEdit);

    List<PostResponse> getListWherePage(Page page);


    void setHotPost(Long postId);

    void unsetHotPost(Long postId);


    List<PostResponse> getHotListWherePage(Page page);

    int upRecommendation(Post post, User user);

    int upBad(Post post, User user);

}

