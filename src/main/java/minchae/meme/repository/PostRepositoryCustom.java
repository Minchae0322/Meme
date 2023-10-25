package minchae.meme.repository;

import minchae.meme.entity.Post;
import minchae.meme.entity.enumClass.PostType;
import minchae.meme.request.Page;

import java.util.Arrays;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPostList(Page page);

    List<Post> getHotList(Page page);

    List<Post> findPostsByPostTypeAndPage(Page page, PostType postType);


    List<Post> getNoticeList(Page page);

    long getPostCountByPostType(PostType postType);

    long getPostCountByHot();
}
