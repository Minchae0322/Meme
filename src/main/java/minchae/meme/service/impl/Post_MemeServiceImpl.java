package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.Recommendation;
import minchae.meme.entity.UploadFile;
import minchae.meme.entity.User;
import minchae.meme.exception.IsRecommended;
import minchae.meme.exception.PostNotFound;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.RecommendationRepository;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.FileService;
import minchae.meme.service.PostService;
import minchae.meme.store.FileStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Post_MemeServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final RecommendationRepository recommendationRepository;
    private final FileService fileService;

    @Override
    @Transactional
    public void write(PostCreate postCreate) throws IOException {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .user(postCreate.getUser())
                .build();
        fileService.writeList(fileService.saveFiles(postCreate.getImageFiles(), post));
        postRepository.save(post);
    }

    @Override
    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        return PostResponse.builder().build().postToPostResponse(post);
    }

    @Override
    @Transactional
    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public PostResponse update(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        post.update(postEdit);
        return PostResponse.builder().build().postToPostResponse(post);
    }
    @Override
    @Transactional
    public List<PostResponse> getListWherePage(Page page) {
        return postRepository.getPostList(page)
                .stream()
                .map(post -> PostResponse.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .recommendation(post.getRecommendations().size())
                        .bad(post.getBad())
                        .views(post.getViews())
                        .user(post.getUser())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void setHotPost(Long postId) {
       Post post = postRepository.findById(postId)
               .orElseThrow(PostNotFound::new);
       post.setHot(true);
    }

    @Override
    @Transactional
    public void unsetHotPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        post.setHot(false);
    }

    @Override
    public List<PostResponse> getHotListWherePage(Page page) {
        return postRepository.getHotList(page)
                .stream()
                .map(post -> PostResponse.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .recommendation(post.getRecommendations().size())
                        .bad(post.getBad())
                        .views(post.getViews())
                        .user(post.getUser())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int upRecommendation(Post post, User user) {
        if (recommendationRepository.findByPostIdAndUserId(post.getPostId(), user.getId()).size() >= 1) {
            throw new IsRecommended();
        }
        recommendationRepository.save(Recommendation.builder()
                .user(user)
                .post(post)
                .build());

        return post.getRecommendations().size() + 1;
    }

}
