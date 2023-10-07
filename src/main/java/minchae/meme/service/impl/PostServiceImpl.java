package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.*;
import minchae.meme.entity.enumClass.PostType;
import minchae.meme.exception.IsRecommended;
import minchae.meme.exception.PostNotFound;
import minchae.meme.exception.Unauthorized;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.UpDownRepository;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.FileService;
import minchae.meme.service.PostService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UpDownRepository updownRepository;
    private final FileService fileService;



    @Override
    @Transactional
    public Post write(PostCreate postCreate) {  // 첨부 이미지) throws IOException {
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .author(postCreate.getUser())
                .postFunction(postFunction)
                .youtubeUrl(postCreate.getYoutubeUrl())
                .postType(PostType.valueOf(postCreate.getPostType()))
                .build();
        postRepository.save(post);
        return post;
    }


    @Override
    @Transactional
    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        post.upView();
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
        if (!post.getAuthor().getUsername().equals(postEdit.getUser().getUsername())) {
            throw new Unauthorized();
        }
        post.update(postEdit);
        return PostResponse.builder().build().postToPostResponse(post);
    }
    @Override
    @Transactional
    public List<PostResponse> getListWherePage(Page page) {
        return postRepository.getPostList(page)
                .stream()
                .map(post -> PostResponse.builder().build().postToPostResponse(post))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void setHotPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        PostFunction postFunction = post.getPostFunction();
        postFunction.setHot(true);
        post.setPostFunction(postFunction);
    }

    @Override
    @Transactional
    public void unsetHotPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        PostFunction postFunction = post.getPostFunction();
        postFunction.setHot(false);
        post.setPostFunction(postFunction);
    }

    @Override
    public long getPostsCount() {
        return postRepository.count();
    }

    @Override
    public List<PostResponse> getPostListByPostType(Page page, PostType postType) {
        return postRepository.findPostsByPostTypeAndPage(page, postType)
                .stream()
                .map(post -> PostResponse.builder()
                        .build().postToPostResponse(post))
                .collect(Collectors.toList());
    }


    @Override
    public List<PostResponse> getHotListWherePage(Page page) {
        return postRepository.getHotList(page)
                .stream()
                .map(post -> PostResponse.builder().build().postToPostResponse(post))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int upRecommendation(Long postId, User user) {
        if (user == null) {
            throw new Unauthorized();
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        if (!updownRepository.findByPostIdAndUserId(post.getPostId(), user.getId()).isEmpty()) {
            throw new IsRecommended();
        }
        updownRepository.save(UpDown.builder()
                .user(user)
                .post(post)
                .type("UP")
                .build());
        //Transactional 이라 데이터 베이스에 저장하기전 값 + 1
        return post.getRecommendation();
    }

    @Override
    @Transactional
    public int upBad(Long postId, User user) {
        if (user == null) {
            throw new Unauthorized();
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        if (!updownRepository.findByPostIdAndUserId(post.getPostId(), user.getId()).isEmpty()) {
            throw new IsRecommended();
        }
        updownRepository.save(UpDown.builder()
                .user(user)
                .post(post)
                .type("DOWN")
                .build());
        return post.getBad();
    }

}
