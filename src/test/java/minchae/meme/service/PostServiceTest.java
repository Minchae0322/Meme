package minchae.meme.service;

import minchae.meme.entity.Post;
import minchae.meme.entity.PostCreate;
import minchae.meme.entity.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.PostService;
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
    private PostService postService;

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
        postService.writePost_Meme(postMeme);
        assertEquals(postRepository.count(), 1);
    }

    @Test
    @DisplayName("게시물1개 조회")
    void getPost() {
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.writePost_Meme(postMeme);
        assertEquals(postRepository.count(), 1);
        Post postResponse = postRepository.findAll().get(0);
        assertEquals(postResponse.getTitle(), "첫게시물입니다");
        assertEquals(postResponse.getContent(), "ㅇㅇㅇㅇㅇㅇ");
        assertEquals(postResponse.getViews(), 0);
    }
}
