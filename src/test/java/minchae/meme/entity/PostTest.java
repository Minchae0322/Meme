package minchae.meme.entity;

import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PostTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostServiceImpl postService;

    @BeforeEach
    public void before() {
        // commentRepository.deleteAll();
        //postRepository.deleteAll();
    }

    @Test
    void test1() throws Exception {
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        List<Comment> comments = IntStream.range(0, 1)
                .mapToObj(i -> Comment.builder()
                        .post(post)
                        .comment("댓글" + " " + i)
                        .build()).collect(Collectors.toList());

        commentRepository.saveAll(comments);
        Post post1 = postRepository.findById(post.getPostId())
                .orElseThrow();
        assertEquals(1, post1.getComments().size());
        //Comment comment = commentRepository.findById(comments.get(0).getCommentId())
                //.orElseThrow();
       // comment.getPost();
    }

}