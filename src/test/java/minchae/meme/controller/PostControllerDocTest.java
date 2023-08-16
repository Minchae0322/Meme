package minchae.meme.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.PostCreate;
import minchae.meme.service.CommentService;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Post_MemeServiceImpl postService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;


    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();

        commentRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    public void writePost() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성 제목")
                .content("글 내용")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String postCreateJson = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/writePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postCreateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("post-post", requestFields(
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("content").description("게시글 내용"),
                        fieldWithPath("writerId").description("글 작성자 ID")
                )))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물1개 조회")
    public void getPost() throws Exception {
        Post postCreate = Post.builder()
                .title("글 작성 제목")
                .content("글 내용")
                .build();

        postRepository.save(postCreate);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", postCreate.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("post-get", pathParameters(
                        parameterWithName("postId").description("게시글 ID")),
                        responseFields(
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("views").description("조회수"),
                                fieldWithPath("writerId").description("글 작성자 id"),
                                fieldWithPath("comments").description("댓글"),
                                fieldWithPath("recommendation").description("추천 수"),
                                fieldWithPath("bad").description("비추천 수 ")
                        )
                ))
                .andDo(print());

    }


    //todo 모든 api 만들기......
}
