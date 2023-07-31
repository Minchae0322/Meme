package minchae.meme.service;

import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.request.PostCreate;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.PostEdit;
import minchae.meme.response.CommentResponse;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

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
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);
    }

    @Test
    @DisplayName("게시물1개 조회")
    void getPost() {
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);
        Post postResponse = postRepository.findAll().get(0);
        assertEquals(postResponse.getTitle(), "첫게시물입니다");
        assertEquals(postResponse.getContent(), "ㅇㅇㅇㅇㅇㅇ");
        assertEquals(postResponse.getViews(), 0);
    }
    @Test
    @DisplayName("게시물 삭제")
    void deletePost() {
        //given
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("ㅇㅇㅇㅇㅇㅇ")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);



        //when
        Post post = postRepository.findAll().get(0);



        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());
        commentRepository.saveAll(comments);

        postService.delete(post.getPostId());

        //result
        assertEquals(postRepository.count(), 0);
        List<Comment> commentList = commentRepository.getCommentListWherePostId(post.getPostId());
        assertEquals(0, commentList.size());

    }


    @Test
    @DisplayName("게시물 수정")
    void updatePost() {
        //given
        PostCreate postMeme = PostCreate.builder()
                .title("첫게시물입니다")
                .content("변경될 내용입니다")
                .build();
        postService.write(postMeme);
        assertEquals(postRepository.count(), 1);

        //when
        Post post = postRepository.findAll().get(0);
        PostEdit postEdit = PostEdit.builder()
                .title("첫게시물입니다")
                .content("내용을 변경합니다")
                .build();
        postService.update(post.getPostId(), postEdit);


        Post updatedPost = postRepository.findById(post.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("수정오류"));
        //result
        assertEquals(postRepository.count(), 1);
        assertEquals(updatedPost.getTitle(), "첫게시물입니다");
        assertEquals(updatedPost.getContent(), "내용을 변경합니다");

    }

    @Test
    @DisplayName("게시물 리스트")
    void getPostListWherePage() {
        //given
        List<Post> posts = IntStream.range(0, 10)
                        .mapToObj(i -> Post.builder()
                                .title("제목" + " " + i)
                                .content("내용" + " " + i)
                                .writerId((long) i)
                                .build())
                                .collect(Collectors.toList());
        postRepository.saveAll(posts);
        assertEquals(postRepository.count(), 10);

        //when
        Post post1 = postRepository.findAll().get(0);
        Post post2 = postRepository.findAll().get(5);


        //result
        assertEquals(10, postRepository.count());
        assertEquals("제목 0", post1.getTitle());
        assertEquals("제목 5", post2.getTitle());

    }


    @Test
    @DisplayName("comment 에만 연관관계 설정을 해줬을때 post 에서도 적용이 되나")
    void getCommentListWherePage() {
        //given
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

        List<Comment> comments = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        List<Comment> comments2 = IntStream.range(0, 30)
                .mapToObj(i -> Comment.builder()
                        .post(post2)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);
        commentRepository.saveAll(comments2);


        // todo assertEquals(30, postRepository.findAll().get(0).getComments().size());
        // 이것은 실패함 post에서도 comments.addAll(comments)를 해줘야 될것으로 예상
    }




}
