package minchae.meme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.UserRepository;
import minchae.meme.request.FreeBoardPage;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.Page;
import minchae.meme.request.PostCreate;
import minchae.meme.request.PostEdit;
import minchae.meme.response.PostResponse;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Post_MemeServiceImpl postService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물 작성")
    void writePost() {

        //when
        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postRepository.save(post);
        assertEquals(postRepository.count(), 1);

        Post savedPost = postRepository.findById(post.getPostId())
                .orElseThrow();
        assertEquals("첫게시물입니다", savedPost.getTitle());
        assertEquals("ㅇㅇㅇㅇㅇㅇ", savedPost.getContent());
        assertEquals(0, savedPost.getComments().size());
    }

    @Test
    @DisplayName("게시물1개 조회")
    void getPost() {
        //given
        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postRepository.save(post);
        assertEquals(postRepository.count(), 1);

        //when
        PostResponse postResponse = postService.get(post.getPostId());

        assertEquals(postResponse.getTitle(), "첫게시물입니다");
        assertEquals(postResponse.getContent(), "ㅇㅇㅇㅇㅇㅇ");
        assertEquals(postResponse.getViews(), 0);
    }
    @Test
    @DisplayName("게시물 삭제")
    void deletePost() {
        //given
        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postRepository.save(post);
        assertEquals(postRepository.count(), 1);


        //when
        Post savedPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        postService.delete(savedPost.getPostId());

        //result
        assertEquals(postRepository.count(), 0);

    }

    @Test
    @DisplayName("게시물을 삭제했을때 게시물에 달린 댓글들 모두 삭제")
    void deletePostAndCheckWhetherCommentsAlsoDelete() {
        //given
        Post post = Post.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postRepository.save(post);
        assertEquals(postRepository.count(), 1);

        //when
        Post savedPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(savedPost)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());
        commentRepository.saveAll(comments);

        postService.delete(savedPost.getPostId());

        //result
        assertEquals(postRepository.count(), 0);
        List<Comment> commentList = commentRepository.getCommentListWherePostId(savedPost.getPostId());
        assertEquals(0, commentList.size());

    }


    @Test
    @DisplayName("게시물 수정")
    void updatePost() {
        //given
        Post postMeme = Post.builder()
                .title("첫게시물입니다")
                .content("변경될 내용입니다")
                .build();
        postRepository.save(postMeme);
        assertEquals(postRepository.count(), 1);

        //when

        PostEdit postEdit = PostEdit.builder()
                .title("첫게시물입니다")
                .content("내용을 변경합니다")
                .build();
        postService.update(postMeme.getPostId(), postEdit);

        Post updatedPost = postRepository.findById(postMeme.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("수정오류"));

        //result
        assertEquals(postRepository.count(), 1);
        assertEquals(updatedPost.getTitle(), "첫게시물입니다");
        assertEquals(updatedPost.getContent(), "내용을 변경합니다");

    }

    @Test
    @DisplayName("게시물 리스트 받아오기")
    void getPostListWherePage() {
        //given
        List<Post> posts = IntStream.range(0, 10)
                        .mapToObj(i -> Post.builder()
                                .title("제목" + " " + i)
                                .content("내용" + " " + i)

                                .build())
                                .collect(Collectors.toList());
        postRepository.saveAll(posts);
        assertEquals(postRepository.count(), 10);

        //when
        //todo 페이지 하나에 몇개의 게시물이 들어가는지
        List<PostResponse> postResponseList = postService.getListWherePage(new FreeBoardPage(1, 5));
        PostResponse post1 = postResponseList.get(0);
        PostResponse post2 = postResponseList.get(4);


        //result
        assertEquals(5, postResponseList.size());
        assertEquals(10, postRepository.count());
        assertEquals("제목 0", post1.getTitle());
        assertEquals("제목 4", post2.getTitle());

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


        Post postCreate = Post.builder()
                .title("글 작성중입니다")
                .content("글 내용은 비밀입니다")
                .user(user)
                .build();

        postRepository.save(postCreate);

        assertEquals(1, postRepository.count());
        assertEquals("wjdalsco", postRepository.findAll().get(0).getUser().getUsername());

    }

    @Test
    @DisplayName("admin 핫 게시물을 설정해준다.")
    void selectHotPost() throws Exception{
        User user = User.builder()
                .username("jmcabc@naver.com")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);


        Post post = Post.builder()
                .title("핫 게시물")
                .user(user)
                .content("핫 게시물 내용입니다.")
                .build();

        postRepository.save(post);

        assertEquals(0, postRepository.getHotList(new Page(1, 10)).size());

        postService.setHotPost(post.getPostId());

        assertEquals(1, postRepository.getHotList(new Page(1, 10)).size());

    }


    @Test
    @DisplayName("admin 핫 게시물을 해제")
    void cancelHotPost() throws Exception{
        User user = User.builder()
                .username("jmcabc@naver.com")
                .email("jcmcmdmw@nakejqkqlw.com")
                .password(passwordEncoder.encode("wjdals12"))
                .enable(true)
                .authorizations(Authorization.USER)
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("핫 게시물")
                .user(user)
                .content("핫 게시물 내용입니다.")
                .build();

        postRepository.save(post);

        postService.setHotPost(post.getPostId());

        assertEquals(1, postRepository.getHotList(new Page(1, 10)).size());

        postService.unsetHotPost(post.getPostId());

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

        List<Post> posts = IntStream.range(0, 20).mapToObj(i -> Post.builder()
                        .title("핫 게시물")
                        .user(user)
                        .content("핫 게시물 내용입니다.")
                        .build()).
                collect(Collectors.toList());

        postRepository.saveAll(posts);

        IntStream.range(0, 10).forEach(i -> postService.setHotPost(posts.get(i).getPostId()));

        assertEquals(10, postRepository.getHotList(new Page(1, 10)).size());
    }




}
