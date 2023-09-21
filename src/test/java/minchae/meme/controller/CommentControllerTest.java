package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import minchae.meme.entity.*;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.exception.CommentNotFound;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.request.CommentVo;
import minchae.meme.service.CommentService;
import minchae.meme.service.PostService;
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
    private UserRepository userRepository;

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
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .author(user)
                .postFunction(postFunction)
                .build();

        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();
        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());
        commentRepository.saveAll(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}/comments", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(30))
                .andDo(print());

        assertEquals(30, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("댓글 작성하기")
    public void writeComment() throws Exception {
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        Post postCreate = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("댓글을 입력하세요")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(postCreate);

        CommentCreate commentCreate = CommentCreate.builder()
                .comment("댓글 1")
                .user(user)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{postId}/comments", postCreate.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(1, commentRepository.count());

    }

    @Test
    @DisplayName("댓글 작성 삽입이상 확인")
    void writeComment2() throws Exception {
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentCreate comment = CommentCreate.builder()

                .comment("댓글입니다")
                .user(user)
                .build();

        CommentCreate comment2 = CommentCreate.builder()

                .comment("댓글입니다2")
                .user(user)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{postId}/comments", post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{postId}/comments", post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].comment").value("댓글입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 작성 후 갱신이상 확인")
    void writeComment3() throws Exception{

        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .user(user)
                .commentFunction(commentFunction)
                .build();

        commentRepository.save(comment);

        //when - 댓글이 바뀌였을때 post 에 있는 List<comment> comments 안에 있는 댓글도 바뀌어야 한다.
        CommentEdit commentEdit = CommentEdit.builder()
                .post(post)
                .comment("바뀐 댓글입니다")
                .author(user)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/board/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("바뀐 댓글입니다"))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].comment").value("바뀐 댓글입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 작성 후 삭제이상 확인")
    void writeComment4() throws Exception{
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();
        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .user(user)
                .commentFunction(commentFunction)
                .build();

        commentRepository.save(comment);

        assertEquals(1, commentRepository.count());

        //when - 댓글이 삭제되었을 때 post 에 있는 comments 안에서도 삭제되어야 한다.
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.size()").value(0))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 1개 조회")
    void getComment() throws Exception{
             User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();

        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .user(user)
                .commentFunction(commentFunction)
                .build();

        commentRepository.save(comment);

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/comment/{commentId}", comment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("댓글입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("포스트에 맞는 댓글 리스트 삽입이상 확인")
    void getCommentListWherePostId1() throws Exception {

        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();

        Post post2 =  Post.builder()
                .title("댓글이 있는 글입니다2")
                .content("메롱2")
                .postFunction(postFunction)
                .author(user)
                .build();

        postRepository.save(post);
        postRepository.save(post2);

        List<CommentCreate> comments = IntStream.range(0, 30)
                .mapToObj(i -> CommentCreate.builder()
                        .post(post)
                        .user(user)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        List<CommentCreate> comments2 = IntStream.range(0, 30)
                .mapToObj(i -> CommentCreate.builder()
                        .post(post2)
                        .user(user)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        CommentVo commentVo = new CommentVo(comments);

        System.out.println(">>>>>>>>>>>>" + objectMapper.writeValueAsString(comments));


        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{postId}/commentList", post.getPostId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentVo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        commentVo = new CommentVo(comments2);

        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{postId}/commentList", post2.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentVo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        Post post1 = postRepository.findById(post.getPostId())
                .orElseThrow();
        assertEquals(30, post1.getComments().size());

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.size()").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].comment").value("댓글 0"))
                .andDo(print());



    }

    @Test
    @DisplayName("댓글 내용 수정하기")
    public void updateComment() throws Exception {
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();
        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .user(user)
                .commentFunction(commentFunction)
                .build();

        commentRepository.save(comment);

        //when - 댓글이 바뀌였을때 post 에 있는 List<comment> comments 안에 있는 댓글도 바뀌어야 한다.
        CommentEdit commentEdit = CommentEdit.builder()
                .comment("바뀐 댓글입니다")
                .author(user)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/board/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("바뀐 댓글입니다"))
                .andDo(print());

    }

    @Test
    @DisplayName("다른 사람이 댓글 내용 수정할때 throw Exception")
    public void updateCommentAny() throws Exception {
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();

        User user2 = User.builder()
                .username("wjdalscod123")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.1231encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();
        Comment comment = Comment.builder()
                .post(post)
                .comment("댓글입니다")
                .user(user)
                .commentFunction(commentFunction)
                .build();

        commentRepository.save(comment);

        //when - 댓글이 바뀌였을때 post 에 있는 List<comment> comments 안에 있는 댓글도 바뀌어야 한다.
        CommentEdit commentEdit = CommentEdit.builder()
                .comment("바뀐 댓글입니다")
                .author(user2)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/board/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEdit)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 삭제하기 where commentId")
    public void deleteComment() throws Exception {
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .commentFunction(commentFunction)
                .comment("댓글입니다")
                .build();

        commentRepository.save(comment);

        assertEquals(1, commentRepository.count());

        //when - 댓글이 삭제되었을 때 post 에 있는 comments 안에서도 삭제되어야 한다.
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/posts/{postId}/comments/{commentId}", post.getPostId(), comment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(0, commentRepository.count());

    }

    @Test
    @DisplayName("댓글리스트 삭제 where PostId")
    @Transactional
    void deleteCommentList() throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();
        //given

        Post post2 =  Post.builder()
                .title("댓글이 있는 글입니다2")
                .content("메롱2")
                .author(user)
                .postFunction(postFunction)
                .build();

        postRepository.save(post);
        postRepository.save(post2);

        CommentFunction commentFunction = CommentFunction.builder().build();
        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .user(user)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        List<Comment> comments2 = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post2)
                        .user(user)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);
        commentRepository.saveAll(comments2);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/posts/{postId}/commentList", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        //result
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments.length()").value(0))
                .andDo(print());

    }

    @Test
    @DisplayName("post 객체 삭제시 post 와 연관된 comments 전부 삭제")
    void deleteCommentListWhenPostDeleted() throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();


        Post post2 =  Post.builder()
                .title("댓글이 있는 글입니다2")
                .content("메롱2")
                .author(user)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);
        postRepository.save(post2);


        CommentFunction commentFunction = CommentFunction.builder().build();
        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .user(user)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        List<Comment> comments2 = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post2)
                        .commentFunction(commentFunction)
                        .user(user)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);
        commentRepository.saveAll(comments2);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertThrows(CommentNotFound.class, () -> commentService.getComment(comments.get(0).getCommentId()));

    }

    /*@Test
    @DisplayName("추천을 1회 올리기 갱신이상 확인")
    void checkUpdateCommentRecommendation() throws Exception {

        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();

        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();
        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .user(user)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);

        //when 댓글의 추천을 눌렀을때
        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{commentId}/up", comments.get(1).getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recommendation").value(1))
                .andDo(print());

        //result 댓글 1의 추천수가 1
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[1].recommendation").value(1))
                .andDo(print());

    }

    @Test
    @DisplayName("추천을 1회 올리기")
    void upCommentRecommendation() throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();

        postRepository.save(post);

        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);

        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{commentId}/up", comments.get(1).getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recommendation").value(1))
                .andDo(print());
    }


    @Test
    @DisplayName("비추천을 1회 올리기")
    void upCommentBad() throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();

        postRepository.save(post);


        CommentFunction commentFunction = CommentFunction.builder().build();
        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .user(user)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);

        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{commentId}/bad", comments.get(1).getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bad").value(1))
                .andDo(print());

    }*/

   /* @Test
    @DisplayName("비추천을 1회 올리기 갱신이상 확인")
    void checkUpdateCommentBad() throws Exception{
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();
        //given
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .author(user)
                .postFunction(postFunction)
                .build();

        postRepository.save(post);

        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);

        //when - 댓글 1의 비추천을 1회 눌렀을때
        mockMvc.perform(MockMvcRequestBuilders.post("/board/posts/{commentId}/bad", comments.get(1).getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bad").value(1))
                .andDo(print());

        //result - 댓글 1의 비추천이 1
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[1].bad").value(1))
                .andDo(print());
    }*/

    @Test
    @DisplayName("댓글 받아오기 - commentNotFound 404 에러 출력")
    void commentNotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/comment/{commentId}", 3933123133L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
        assertThrows(CommentNotFound.class,() -> {
            commentService.getComment(1L);
        });

    }

}