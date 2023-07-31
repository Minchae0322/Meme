package minchae.meme.service;

import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.response.CommentResponse;
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
    private CommentService commentService;

    @Autowired
    private Post_MemeServiceImpl postService;

    @BeforeEach
    public void before() {
       // commentRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void test1() throws Exception {
        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);
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

        List<CommentResponse> commentList = commentService.getCommentList(post.getPostId());

        Assertions.assertEquals(30, commentList.size());
        Assertions.assertEquals("댓글 4", commentList.get(4).getComment());
    }

    @Test
    @DisplayName("댓글 작성")
    void writeComment() {

        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        CommentCreate comment = CommentCreate.builder()
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentService.write(post, comment);

        Comment writedComment = commentRepository.findAll().get(0);

        Assertions.assertEquals(commentRepository.findAll().size(), 1);
        Assertions.assertEquals("댓글입니다", writedComment.getComment());
        assertEquals("댓글이 있는 글입니다", writedComment.getPost().getTitle());
    }

    @Test
    @DisplayName("댓글 1개 조회")
    void getComment() {

        Post post = Post.builder()
                .title("댓글이 있는 글입니다")
                .content("메롱")
                .build();
        postRepository.save(post);

        CommentCreate comment = CommentCreate.builder()
                .comment("댓글입니다")
                .writerId(24L)
                .build();

        commentService.write(post, comment);

        CommentResponse writedComment = commentService.getComment(commentRepository.findAll().get(0).getCommentId());

        Assertions.assertEquals("댓글입니다", writedComment.getComment());
        assertEquals("댓글이 있는 글입니다", writedComment.getPost().getTitle());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
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

        CommentEdit commentEdit = CommentEdit.builder()
                .comment("수정한 댓글입니다")
                .post(post)
                .commentId(comment.getCommentId())
                .writerId(24L)
                .build();

        commentService.update(commentEdit.getCommentId(), commentEdit);

        assertEquals(commentRepository.findAll().size(), 1);
        assertEquals("수정한 댓글입니다", commentService.getComment(comment.getCommentId()).getComment());
        assertEquals(0, commentService.getComment(comment.getCommentId()).getRecommendation());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
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
        commentService.delete(comment.getCommentId());

        assertEquals(0, commentRepository.findAll().size());

    }


    @Test
    @DisplayName("댓글리스트 삭제 where PostId")
    void deleteCommentList() {
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

        commentService.deleteCommentList(post.getPostId());

        List<Comment> commentList = commentRepository.getCommentListWherePostId(post.getPostId());

        assertEquals(0, commentList.size());


    }
}