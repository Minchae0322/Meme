package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.request.PostCreate;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.PostEdit;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Post_MemeServiceImpl postService;

    @BeforeEach
    public void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물 작성")
    void writePost() {
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);
    }

    @Test
    @DisplayName("게시물1개 조회")
    void getPost() {
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);
        Post postResponse = postRepository.findAll().get(0);
        assertEquals(postResponse.getTitle(), "첫게시물입니다");
        assertEquals(postResponse.getContent(), "ㅇㅇㅇㅇㅇㅇ");
        assertEquals(postResponse.getViews(), 0);
    }
    @Test
    @DisplayName("게시물 삭제")
    void deletePost() {
        //given
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);

        //when
        Post post = postRepository.findAll().get(0);
        postService.delete(post.getPostId());


        //result
        assertEquals(postRepository.count(), 0);

    }


    @Test
    @DisplayName("게시물 수정")
    void updatePost() {
        //given
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("변경될 내용입니다")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);

        //when
        Post post = postRepository.findAll().get(0);
        PostEdit postEdit = PostEdit.builder()
                .title("첫게시물입니다")
                .content("내용을 변경합니다")
                .build();
        postService.update(post.getPostId(), postEdit);


        Post updatedPost = postRepository.findById(post.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("수정오류"));
        //result
        assertEquals(postRepository.count(), 1);
        assertEquals(updatedPost.getTitle(), "첫게시물입니다");
        assertEquals(updatedPost.getContent(), "내용을 변경합니다");

    }


}
