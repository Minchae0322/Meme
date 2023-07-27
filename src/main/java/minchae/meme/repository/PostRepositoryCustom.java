package minchae.meme.repository;

import minchae.meme.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPostList(int page);
}
