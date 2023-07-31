package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.PostCreate;
import minchae.meme.service.CommentService;
import minchae.meme.service.impl.CommentServiceImpl;
import minchae.meme.service.impl.Post_MemeServiceImpl;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private Post_MemeServiceImpl postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 불러오기 where postId")
    public void getCommentListWherePostId() throws Exception {
        Post post  = Post.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .writerId(1L)
                .build();

        postRepository.save(post);

        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());
        commentRepository.saveAll(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}/comments", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(30))
                .andDo(print());

        assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("댓글 작성하기")
    public void writeComment() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("댓글이 있는 글입니다")
                .content("댓글을 입력하세요")
                .writerId(20L)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        CommentCreate commentCreate = CommentCreate.builder()
                .comment("댓글 1")
                .writerId(30L)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comment", postRepository.findAll().get(0).getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 하나 가져오기 where commentId")
    public void getComment() throws Exception {
       //todo

    }

    @Test
    @DisplayName("댓글 내용 수정하기")
    public void updateComment() throws Exception {
        //todo

    }

    @Test
    @DisplayName("댓글 삭제하기 where commentId")
    public void deleteComment() throws Exception {
        //todo

    }
}