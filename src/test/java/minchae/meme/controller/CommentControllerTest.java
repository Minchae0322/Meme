package minchae.meme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.request.CommentVo;
import minchae.meme.request.PostCreate;
import minchae.meme.response.CommentResponse;
import minchae.meme.response.PostResponse;
import minchae.meme.service.CommentService;
import minchae.meme.service.PostService;
import minchae.meme.service.impl.CommentServiceImpl;
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
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }



    @Test
    @DisplayName("댓글 불러오기 where postId")
    public void getCommentListWherePostId() throws Exception {
        Post post = Post.builder()
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

        assertEquals(30, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("댓글 작성하기")
    public void writeComment() throws Exception {
        Post postCreate = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("댓글을 입력하세요")
                .writerId(20L)
                .build();
        postRepository.save(postCreate);

        CommentCreate commentCreate = CommentCreate.builder()
                .comment("댓글 1")
                .writerId(30L)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comments", postCreate.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(1, commentRepository.count());

    }

    @Test
    @DisplayName("댓글 작성 삽입이상 확인")
    void writeComment2() throws Exception {

        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        CommentCreate comment = CommentCreate.builder()
                .post(post)
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        CommentCreate comment2 = CommentCreate.builder()
                .post(post)
                .comment("댓글입니다2")
                .writerId(24L)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comments", post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comments", post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].comment").value("댓글입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 작성 후 갱신이상 확인")
    void writeComment3() throws Exception{
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentRepository.save(comment);

        //when - 댓글이 바뀌였을때 post 에 있는 List<comment> comments 안에 있는 댓글도 바뀌어야 한다.
        CommentEdit commentEdit = CommentEdit.builder()
                .comment("바뀐 댓글입니다")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("바뀐 댓글입니다"))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].comment").value("바뀐 댓글입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 작성 후 삭제이상 확인")
    void writeComment4() throws Exception{
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentRepository.save(comment);

        assertEquals(1, commentRepository.count());

        //when - 댓글이 삭제되었을 때 post 에 있는 comments 안에서도 삭제되어야 한다.
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.size()").value(0))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 1개 조회")
    void getComment() throws Exception{

        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentRepository.save(comment);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/comment/{commentId}", comment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("댓글입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.writerId").value(24L))
                .andDo(print());
    }

    @Test
    @DisplayName("포스트에 맞는 댓글 리스트 삽입이상 확인")
    void getCommentListWherePostId1() throws Exception {

        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();

        Post post2 =  Post.builder()
                .title("댓글이 있는 글입니다2")
                .content("메롱2")
                .build();

        postRepository.save(post);
        postRepository.save(post2);

        List<CommentCreate> comments = IntStream.range(0, 30)
                .mapToObj(i -> CommentCreate.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        List<CommentCreate> comments2 = IntStream.range(0, 30)
                .mapToObj(i -> CommentCreate.builder()
                        .post(post2)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        CommentVo commentVo = new CommentVo(comments);


        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/commentList", post.getPostId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentVo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        commentVo = new CommentVo(comments2);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/commentList", post2.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentVo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        Post post1 = postRepository.findById(post.getPostId())
                .orElseThrow();
        assertEquals(30, post1.getComments().size());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.size()").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].comment").value("댓글 0"))
                .andDo(print());



    }

    @Test
    @DisplayName("포스트에 맞는 댓글 리스트 갱신이상 확인")
    void getCommentListWherePostId2() {

    }
    @Test
    @DisplayName("댓글 내용 수정하기")
    public void updateComment() throws Exception {
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentRepository.save(comment);

        //when - 댓글이 바뀌였을때 post 에 있는 List<comment> comments 안에 있는 댓글도 바뀌어야 한다.
        CommentEdit commentEdit = CommentEdit.builder()
                .comment("바뀐 댓글입니다")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("바뀐 댓글입니다"))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 삭제하기 where commentId")
    public void deleteComment() throws Exception {
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentRepository.save(comment);

        assertEquals(1, commentRepository.count());

        //when - 댓글이 삭제되었을 때 post 에 있는 comments 안에서도 삭제되어야 한다.
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(0, commentRepository.count());

    }

    @Test

    @DisplayName("댓글리스트 삭제 where PostId")
    @Transactional
    void deleteCommentList() {
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        //todo
    }

    @Test
    @DisplayName("post 객체 삭제시 post 와 연관된 comments 전부 삭제")
    void deleteCommentListWhenPostDeleted() {
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        //todo

    }

    @Test
    @DisplayName("추천을 1회 올리기")
    void upCommentRecommendation() {
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        //todo

    }


    @Test
    @DisplayName("비추천을 1회 올리기")
    void upCommentBad() {
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        //todo

    }

}