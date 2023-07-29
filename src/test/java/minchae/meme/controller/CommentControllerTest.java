package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
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
    }
}