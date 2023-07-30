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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
                .postId(post.getPostId())
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
        //todo 글에 관련된 댓글도 모두 삭제되어야함.
        //todo 이렇게 된다면 post 에 List<Comment> comments 를 추가시켜야 할 수 밖엥 없는가?
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public PostResponse update(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 게시물 입니다"));
        post.update(postEdit);
        return PostResponse.builder().build().postToPostResponse(post);
    }
    @Override
    @Transactional
    public List<PostResponse> getListWherePage(int page) {
        return postRepository.getPostList(page)
                .stream()
                .map(post -> PostResponse.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .recommendation(post.getRecommendation())
                        .bad(post.getBad())
                        .views(post.getViews())
                        .writerId(post.getWriterId())
                        .build())
                .collect(Collectors.toList());
    }


}
