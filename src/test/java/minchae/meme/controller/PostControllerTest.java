package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Post;
import minchae.meme.request.PostCreate;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.PostEdit;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private Post_MemeServiceImpl postService;

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
        postService.write(postCreate);

        Post postResponse = postRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", postResponse.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 작성중입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용은 비밀입니다"))
                .andDo(print());
    }
    @Test
    @DisplayName("글 삭제")
    public void deletePost() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .writerId(1L)
                .build();
        postService.write(postCreate);


        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", postResponse.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 0);
    }

    @Test
    @DisplayName("글 수정")
    //todo controller에 메소드 만들기
    public void updatePost() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .writerId(1L)
                .build();
        postService.write(postCreate);


        PostEdit postEdit = PostEdit.builder()
                .title("글 내용을 변경하겠습니다")
                .content("글 내용을 변경합니다")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(postEdit);

        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", postResponse.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 내용을 변경하겠습니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용을 변경합니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(postResponse.getPostId()))
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 1);
    }
}