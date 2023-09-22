package minchae.meme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.entity.*;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.entity.enumClass.PostType;
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
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static String ACCESS_TOKEN;

    private User testUser;

    @BeforeEach
    public void before(@Value("${jwt.secret}") String secretKey) {
       commentRepository.deleteAll();
       postRepository.deleteAll();
       userRepository.deleteAll();
       upDownRepository.deleteAll();
       testUser = User.builder()
                .username("jmcabddc")
                .email("jmcabc5555@naver.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
       userRepository.save(testUser);
       byte[] keyBytes = Decoders.BASE64.decode(secretKey);
       long now = new Date().getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 604800000);
        ACCESS_TOKEN = Jwts.builder()
                .setSubject(testUser.getUsername())
                .claim("auth", testUser.getAuthorizations())
                .setExpiration(accessTokenExpiresIn)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

/************************** post 작성과 관련된 테스트 *******************************/
    @Test
    @DisplayName("/board/user/postWrite 첨부파일을 포함한 글 작성 테스트")
    public void testWritePost() throws Exception {
        // given
        PostCreate postCreate = new PostCreate();
        postCreate.setTitle("Test Title");
        postCreate.setContent("Test Content");
        postCreate.setPostType("ALL");

        // when
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "test-image.jpg", "image/jpeg", "image data".getBytes()
        );
        String json = new ObjectMapper().writeValueAsString(postCreate);
        MockMultipartFile notice = new MockMultipartFile("post", "post", "application/json", json.getBytes(StandardCharsets.UTF_8));
        // Perform the POST request
        mockMvc.perform(multipart("/board/user/writePost")
                                .file(notice)
                                .file(imageFile)
                                .header("Authorization", ACCESS_TOKEN)
                ).andExpect(status().isOk());

        assertEquals(1, postRepository.count());
    }

    @Test
    @DisplayName("/board/user/postWrite 첨푸파일을 포함하지 않는 글 작성테스트")
    public void testWritePostNoFile() throws Exception {
        // given
        PostCreate postCreate = new PostCreate();
        postCreate.setTitle("Test Title");
        postCreate.setContent("Test Content");
        postCreate.setPostType("ALL");

        String json = new ObjectMapper().writeValueAsString(postCreate);
        MockMultipartFile notice = new MockMultipartFile("post", "post", "application/json", json.getBytes(StandardCharsets.UTF_8));

        // when
        mockMvc.perform(multipart("/board/user/writePost")
                        .file(notice)
                        .header("Authorization", ACCESS_TOKEN)
                ).andExpect(status().isOk());

        assertEquals(1, postRepository.count());
    }

    @Test
    @DisplayName("글 작성 직후 추천 수는 0, 비추천 수도 0, 조회수 0, 핫 게시물은 false 이다.")
    public void initPost() throws Exception {
        PostCreate postCreate = new PostCreate();
        postCreate.setTitle("Test Title");
        postCreate.setContent("Test Content");
        postCreate.setPostType("ALL");

        // when
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "test-image.jpg", "image/jpeg", "image data".getBytes()
        );
        String json = new ObjectMapper().writeValueAsString(postCreate);
        MockMultipartFile notice = new MockMultipartFile("post", "post", "application/json", json.getBytes(StandardCharsets.UTF_8));
        // Perform the POST request
        mockMvc.perform(multipart("/board/user/writePost")
                .file(notice)
                .file(imageFile)
                .header("Authorization", ACCESS_TOKEN)
        ).andExpect(status().isOk());

        assertEquals(1, postRepository.count());

        Post posted = postRepository.findAll().get(0);

        assertEquals(1, postRepository.count());
        assertEquals(0, upDownRepository.count());
        assertFalse(posted.getPostFunction().isHot());
    }


    @Test
    @Transactional
    @DisplayName("글 작성 직후 댓글은 없다.")
    public void initPostComment() throws Exception {
        PostCreate postCreate = new PostCreate();
        postCreate.setTitle("Test Title");
        postCreate.setContent("Test Content");
        postCreate.setPostType("ALL");

        // when
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "test-image.jpg", "image/jpeg", "image data".getBytes()
        );
        String json = new ObjectMapper().writeValueAsString(postCreate);
        MockMultipartFile notice = new MockMultipartFile("post", "post", "application/json", json.getBytes(StandardCharsets.UTF_8));
        // Perform the POST request
        mockMvc.perform(multipart("/board/user/writePost")
                .file(notice)
                .file(imageFile)
                .header("Authorization", ACCESS_TOKEN)
        ).andExpect(status().isOk());

        assertEquals(1, postRepository.count());

        Post posted = postRepository.findAll().get(0);

        assertEquals(1, postRepository.count());
        assertEquals(0, posted.getComments().size());
    }

    @Test
    @DisplayName("게시물은 로그인된 사용자만 작성할 수 있다")
    void authWritePost() throws Exception{
        PostCreate postCreate = new PostCreate();
        postCreate.setTitle("Test Title");
        postCreate.setContent("Test Content");
        postCreate.setPostType("ALL");

        // when
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "test-image.jpg", "image/jpeg", "image data".getBytes()
        );
        String json = new ObjectMapper().writeValueAsString(postCreate);
        MockMultipartFile notice = new MockMultipartFile("post", "post", "application/json", json.getBytes(StandardCharsets.UTF_8));
        // Perform the POST request
        mockMvc.perform(multipart("/board/user/writePost")
                .file(notice)
                .file(imageFile)

        ).andExpect(status().is4xxClientError());
    }


    /**************************************** 글 조회 관련 테스트 ****************************************************/
    @Test
    @DisplayName("게시물1개 조회")
    public void getPost() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(testUser)
                .postType("ALL")
                .build();
        postService.write(postCreate);

        Post postResponse = postRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", postResponse.getPostId())
                        .header("Authorization",ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 작성중입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용은 비밀입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postFunction.hot").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물1개 조회 - 다른 user 가 recommendation 을 눌렀을 때 추천수가 1 상승")
    public void getPost2() throws Exception {
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
                .postType("ALL")
                .build();
        postService.write(postCreate);
        Post postResponse = postRepository.findAll().get(0);


        postService.setHotPost(postResponse.getPostId());
        postService.upRecommendation(postResponse.getPostId(), testUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", postResponse.getPostId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 작성중입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용은 비밀입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postFunction.hot").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recommendation").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 리스트 받아오기")
    void getPostListWherePage() throws Exception{
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("다다다자"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        List<Post> posts = IntStream.range(0, 10)
                .mapToObj(i -> Post.builder()
                        .title("제목" + " " + i)
                        .content("내용" + " " + i)
                        .author(user)
                        .postFunction(postFunction)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);
        assertEquals(postRepository.count(), 10);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/list?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
                .andDo(print());


        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/list?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andDo(print());

    }


    @Test
    @DisplayName("게시물 받아오기 - postNotFound 404 에러 출력")
    void postNotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/{postId}", 393L))
                .andExpect(status().isNotFound())
                .andDo(print());

        assertThrows(PostNotFound.class,() -> {
            postService.get(1L);
        });


    }


    /************************************* 글 삭제 관련 테스트 *********************************************/
    @Test
    @DisplayName("글 삭제")
    public void deletePost() throws Exception {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(testUser)
                .postType("ALL")
                .build();
        postService.write(postCreate);


        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", postResponse.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 0);
    }


    @Test
    @DisplayName("글을 작성한 사용자가 아닌 다른 사용자는 글을 삭제 할 수 없다.")
    public void deletePostWithUserId() throws Exception {
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
                .postType("ALL")
                .build();
        postService.write(postCreate);

        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", postResponse.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 1);
    }


    @Test
    @DisplayName("admin 은 모든 글을 삭제 할 수 있다.")
    public void deletePostAdmin(@Value("${jwt.secret}") String secretKey) throws Exception {
        //given
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.ADMIN)
                .build();
        userRepository.save(user);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        long now = new Date().getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 604800000);
        ACCESS_TOKEN = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("auth", user.getAuthorizations())
                .setExpiration(accessTokenExpiresIn)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();

        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .postType("ALL")
                .build();
        postService.write(postCreate);

        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", postResponse.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 0);
    }

    @Test
    @DisplayName("게시물을 삭제했을때 게시물에 달린 댓글들 모두 삭제")
    void deletePostAndCheckWhetherCommentsAlsoDelete() throws Exception {
        //given
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .author(testUser)
                .postType(PostType.ALL)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        CommentFunction commentFunction = CommentFunction.builder().build();

        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .user(testUser)
                        .commentFunction(commentFunction)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());
        commentRepository.saveAll(comments);

        assertEquals(30, commentRepository.count());
        assertEquals(post.getPostId(), commentRepository.findAll().get(0).getPost().getPostId());
        assertEquals(1, postRepository.count());


        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", post.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());


        //result
        assertEquals(postRepository.count(), 0);
        List<Comment> commentList = commentRepository.getCommentListWherePostId(post.getPostId());
        assertEquals(0, commentList.size());

    }

    @Test
    @DisplayName("게시물을 삭제했을때 게시물에 달린 추천 비추천들 모두 삭제")
    void deletePostAndCheckWhetherUpDownAlsoDelete() throws Exception {
        //given
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .author(testUser)
                .postType(PostType.ALL)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);


        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/user/{postId}", post.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());

        //result
        assertEquals(postRepository.count(), 0);
        assertEquals(0, upDownRepository.count());


    }

/****************************** 글 수정 관련 테스트 **************************************/
    @Test
    @DisplayName("글 수정")
    //todo controller에 메소드 만들기
    public void updatePost() throws Exception {
        //given
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .author(testUser)
                .postType(PostType.ALL)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);


        PostEdit postEdit = PostEdit.builder()
                .title("글 내용을 변경하겠습니다")
                .content("글 내용을 변경합니다")
                .user(testUser)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        java.lang.String json = objectMapper.writeValueAsString(postEdit);

        //when
        Post postResponse = postRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.patch("/board/user/{postId}", postResponse.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 내용을 변경하겠습니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("글 내용을 변경합니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(postResponse.getPostId()))
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 1);
    }

    @Test
    @DisplayName("글 수정은 작성자만 할 수 있다.")
    public void updatePostOnlyWriter(@Value("${jwt.secret}") String secretKey) throws Exception {
        //given
        PostFunction postFunction = PostFunction.builder()
                .isHot(false)
                .build();

        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .author(testUser)
                .postType(PostType.ALL)
                .postFunction(postFunction)
                .build();
        postRepository.save(post);

        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.ADMIN)
                .build();
        userRepository.save(user);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        long now = new Date().getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 604800000);
        ACCESS_TOKEN = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("auth", user.getAuthorizations())
                .setExpiration(accessTokenExpiresIn)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();

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
                        .content(json)
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        Assertions.assertEquals(postRepository.count(), 1);
    }




    @Test
    @DisplayName("admin 핫 게시물을 설정해준다.")
    void selectHotPost(@Value("${jwt.secret}") String secretKey) throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.ADMIN)
                .build();
        userRepository.save(user);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        long now = new Date().getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 604800000);
        ACCESS_TOKEN = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("auth", user.getAuthorizations())
                .setExpiration(accessTokenExpiresIn)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();

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

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/setHot/{postId}", post.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1, postRepository.getHotList(new Page(1, 5)).size());
    }


    @Test
    @DisplayName("admin 핫 게시물을 해제")
    void cancelHotPost(@Value("${jwt.secret}") String secretKey) throws Exception{
        User user = User.builder()
                .username("wjdalsco")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password("passwordEncoder.encode(signupForm.getPassword()")
                .enable(true)
                .authorizations(Authorization.ADMIN)
                .build();
        userRepository.save(user);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        long now = new Date().getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 604800000);
        ACCESS_TOKEN = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("auth", user.getAuthorizations())
                .setExpiration(accessTokenExpiresIn)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();

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

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/setHot/{postId}", post.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1, postRepository.getHotList(new Page(1, 10)).size());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/unsetHot/{postId}", post.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(0, postRepository.getHotList(new Page(1, 10)).size());
    }



    @Test
    @DisplayName("모든 타입의 핫 게시물을 가져오기")
    void getHotPost() throws Exception{


        PostFunction postFunction = PostFunction
                .builder()
                .isHot(false)
                .build();

        List<Post> posts = IntStream.range(0, 20).mapToObj(i -> Post.builder()
                        .title("핫 게시물")
                        .author(testUser)
                        .postFunction(postFunction)
                        .content("핫 게시물 내용입니다.")
                        .build()).
                collect(Collectors.toList());

        postRepository.saveAll(posts);

        IntStream.range(0, 10).forEach(i -> postService.setHotPost(posts.get(i).getPostId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/board/posts/hotList?page=1&size=10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andDo(print());
    }


    @Test
    @DisplayName("다른 user 가 recommendation 을 눌렀을 때 추천수가 1 상승")
    public void upRecommendation() throws Exception {
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
                .postType("ALL")
                .build();
        postService.write(postCreate);
        Post postResponse = postRepository.findAll().get(0);



        mockMvc.perform(MockMvcRequestBuilders.get("/board/user/{postId}/up", postResponse.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().isOk())

                .andDo(print());
    }


    @Test
    @DisplayName("같은 user 가 recommendation 두번 을 눌렀을 때 throw isRecommended")
    public void upRecommendationSameUser() throws Exception {

        PostCreate postCreate = PostCreate.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(testUser)
                .postType("ALL")
                .build();
        postService.write(postCreate);
        Post postResponse = postRepository.findAll().get(0);

        postService.upBad(postResponse.getPostId(), testUser);



        mockMvc.perform(MockMvcRequestBuilders.post("/board/user/{postId}/up", postResponse.getPostId())
                        .header("Authorization", ACCESS_TOKEN))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}