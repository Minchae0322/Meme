package minchae.meme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Post;
import minchae.meme.entity.PostCreate;
import minchae.meme.entity.PostResponse;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    public void writePost() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .writerId(1L)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String postCreateJson = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postCreateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 작성중입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용은 비밀입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("글1개 조회")
    public void getPost() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .writerId(1L)
                .build();
        postService.writePost_Meme(postCreate);

        Post postResponse = postRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", postResponse.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 작성중입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용은 비밀입니다"))
                .andDo(print());
    }

}