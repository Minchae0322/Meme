package minchae.meme.repository;

import minchae.meme.entity.Post;
import minchae.meme.request.Page;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPostList(Page page);
}
