package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.PostService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Post_MemeServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .writerId(postCreate.getWriterId())
                .build();
        postRepository.save(post);
    }

    @Override
    public PostResponse get(Long postId) {
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

    @Override
    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않거나 이미 삭제된 게시물입니다"));
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void update(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 게시물 입니다"));
        post.update(postEdit);
    }


}
