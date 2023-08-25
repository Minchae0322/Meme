package minchae.meme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.entity.*;
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
import minchae.meme.store.FileStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UpDownRepository updownRepository;
    private final FileService fileService;

    private final FileStore fileStore;

    @Override
    @Transactional
    public void write(PostCreate postCreate, MultipartFile multipartFile) throws IOException {  // 첨부 이미지) throws IOException {
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .author(postCreate.getUser())
                .postFunction(postFunction)
                .build();
        postRepository.save(post);
        fileStore.storeFile(multipartFile, post);
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
    public List<PostResponse> getHotListWherePage(Page page) {
        return postRepository.getHotList(page)
                .stream()
                .map(post -> PostResponse.builder().build().postToPostResponse(post))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int upRecommendation(Post post, User user) {
        if (user == null) {
            throw new Unauthorized();
        }
        if (updownRepository.findByPostIdAndUserId(post.getPostId(), user.getId()).size() >= 1) {
            throw new IsRecommended();
        }
        updownRepository.save(UpDown.builder()
                .user(user)
                .post(post)
                .type("UP")
                .build());
        //Transactional 이라 데이터 베이스에 저장하기전 값 + 1
        return post.getRecommendation() + 1;
    }

    @Override
    @Transactional
    public int upBad(Post post, User user) {
        if (updownRepository.findByPostIdAndUserId(post.getPostId(), user.getId()).size() >= 1) {
            throw new IsRecommended();
        }
        updownRepository.save(UpDown.builder()
                .user(user)
                .post(post)
                .type("DOWN")
                .build());
        return post.getBad() + 1;
    }

}
