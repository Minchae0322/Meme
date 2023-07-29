package minchae.meme.service;

import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.service.impl.Post_MemeServiceImpl;
import org.junit.jupiter.api.Assertions;
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
class CommentServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Post_MemeServiceImpl postService;

    @BeforeEach
    public void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("포스트에 맞는 댓글 리스트 가져오기")
    void getCommentListWherePostId() {

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

        List<Comment> commentList = commentRepository.getCommentListWherePostId(post.getPostId());

        Assertions.assertEquals(30, commentList.size());
        Assertions.assertEquals("댓글 4", commentList.get(4).getComment());
    }
}