package minchae.meme.service;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.PostCreate;
import minchae.meme.entity.PostResponse;
import minchae.meme.entity.Post_Meme;
import minchae.meme.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void writePost_Meme(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .writerId(postCreate.getWriterId())
                .build();
        postRepository.save(post);
    }

    public PostResponse getPost_Meme(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .recommendation(post.getRecommendation())
                .bad(post.getBad())
                .views(post.getViews())
                .writerId(post.getWriterId())
                .build();
    }



}
