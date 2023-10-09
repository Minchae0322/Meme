package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.User;
import minchae.meme.entity.enumClass.PostType;
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

    Post update(Long postId, PostEdit postEdit);

    List<PostResponse> getListWherePage(Page page);


    void setHotPost(Long postId);

    void unsetHotPost(Long postId);

    long getPostsCount();


    List<PostResponse> getPostListByPostType(Page page, PostType postType);

    List<PostResponse> getHotListWherePage(Page page);

    int upRecommendation(Long postId, User user);

    int upBad(Long postId, User user);


}

