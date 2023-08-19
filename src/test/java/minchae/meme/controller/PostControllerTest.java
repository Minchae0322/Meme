package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.*;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.exception.PostNotFound;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.UpDownRepository;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.service.CommentService;
import minchae.meme.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc

class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UpDownRepository upDownRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void before() {
       commentRepository.deleteAll();
       postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    public void writePost() throws Exception {
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        java.lang.String postCreateJson = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/board/user/writePost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postCreateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(1, postRepository.count());
    }

    @Test
    @DisplayName("게시물1개 조회")
    public void getPost() throws Exception {
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .build();
        postService.write(postCreate);

        Post postResponse = postRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", postResponse.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 작성중입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용은 비밀입니다"))
                .andDo(print());
    }
    @Test
    @DisplayName("글 삭제")
    public void deletePost() throws Exception {
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .build();
        postService.write(postCreate);


        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", postResponse.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 0);
    }

    @Test
    @DisplayName("게시물을 삭제했을때 게시물에 달린 댓글들 모두 삭제")
    void deletePostAndCheckWhetherCommentsAlsoDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

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

        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
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

        assertEquals(30, commentRepository.count());
        assertEquals(post.getPostId(), commentRepository.findAll().get(0).getPost().getPostId());

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());


        //result
        assertEquals(postRepository.count(), 0);
        List<Comment> commentList = commentRepository.getCommentListWherePostId(post.getPostId());
        assertEquals(0, commentList.size());

    }

    @Test
    @DisplayName("글 수정")
    //todo controller에 메소드 만들기
    public void updatePost() throws Exception {
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .build();
        postService.write(postCreate);


        PostEdit postEdit = PostEdit.builder()
                .title("글 내용을 변경하겠습니다")
                .content("글 내용을 변경합니다")
                .user(user)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        java.lang.String json = objectMapper.writeValueAsString(postEdit);

        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.patch("/board/user/{postId}", postResponse.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 내용을 변경하겠습니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용을 변경합니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(postResponse.getPostId()))
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 1);
    }

    @Test
    @DisplayName("게시물 리스트 받아오기")
    void getPostListWherePage() throws Exception{
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        List<Post> posts = IntStream.range(0, 10)
                .mapToObj(i -> Post.builder()
                        .title("제목" + " " + i)
                        .content("내용" + " " + i)
                        .author(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);
        assertEquals(postRepository.count(), 10);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/list?page=1&size=5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
                .andDo(print());


        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/list?page=1&size=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andDo(print());

    }


    @Test
    @DisplayName("게시물 받아오기 - postNotFound 404 에러 출력")
    void postNotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", 393L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());

        assertThrows(PostNotFound.class,() -> {
            postService.get(1L);
        });


    }

    @Test
    @DisplayName("게시물은 로그인된 사용자만 작성할 수 있다")
    void authWritePost() throws Exception{
       //todo
    }


    @Test
    @DisplayName("게시물 작성 작성자 포함")
    void writePostWithUser() throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .build();



        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(">>>>>>>>>>>" + " " + objectMapper.writeValueAsString(postCreate));



        mockMvc.perform(MockMvcRequestBuilders.post("/board/user/writePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());



    }


    @Test
    @DisplayName("admin 핫 게시물을 설정해준다.")
    @WithMockUser(authorities = "ADMIN")
    void selectHotPost() throws Exception{
        User user = User.builder()
                .username("jmcabc@naver.com")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);
        PostFunction postFunction = PostFunction
                .builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title("핫 게시물")
                .author(user)
                .postFunction(postFunction)
                .content("핫 게시물 내용입니다.")
                .build();

        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/setHot/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(1, postRepository.getHotList(new Page(1, 10)).size());
    }


    @Test
    @DisplayName("admin 핫 게시물을 해제")
    @WithMockUser(authorities = "ADMIN")
    void cancelHotPost() throws Exception{
        User user = User.builder()
                .username("jmcabc@naver.com")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostFunction postFunction = PostFunction
                .builder()
                .isHot(false)
                .build();
        Post post = Post.builder()
                .title("핫 게시물")
                .author(user)
                .postFunction(postFunction)
                .content("핫 게시물 내용입니다.")
                .build();

        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/setHot/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(1, postRepository.getHotList(new Page(1, 10)).size());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/unsetHot/{postId}", post.getPostId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertEquals(0, postRepository.getHotList(new Page(1, 10)).size());
    }



    @Test
    @DisplayName("admin 모든 타입의 핫 게시물을 가져오기")
    void getHotPost() throws Exception{
        User user = User.builder()
                .username("jmcabc@naver.com")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostFunction postFunction = PostFunction
                .builder()
                .isHot(false)
                .build();

        List<Post> posts = IntStream.range(0, 20).mapToObj(i -> Post.builder()
                        .title("핫 게시물")
                        .author(user)
                        .postFunction(postFunction)
                        .content("핫 게시물 내용입니다.")
                        .build()).
                collect(Collectors.toList());

        postRepository.saveAll(posts);

        IntStream.range(0, 10).forEach(i -> postService.setHotPost(posts.get(i).getPostId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/hotList?page=1&size=10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andDo(print());
    }
}