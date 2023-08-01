package minchae.meme.service;

import jakarta.transaction.Transactional;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import minchae.meme.request.CommentCreate;
import minchae.meme.request.CommentEdit;
import minchae.meme.response.CommentResponse;
import minchae.meme.response.PostResponse;
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
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }




    @Test
    @DisplayName("댓글 작성")
    void writeComment() {

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

        Comment writedComment = commentRepository.findById(comment.getCommentId())
                .orElseThrow();

        Assertions.assertEquals(commentRepository.findAll().size(), 1);
        Assertions.assertEquals("댓글입니다", writedComment.getComment());
        assertEquals("댓글이 있는 글입니다", writedComment.getPost().getTitle());
    }

    @Test
    @DisplayName("댓글 작성 삽입이상 확인")
    void writeComment2() {

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

        Post commentPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        assertEquals(1, commentPost.getComments().size());
        assertEquals(1, commentRepository.count());
        assertEquals("댓글입니다", commentPost.getComments().get(0).getComment());
    }

    @Test
    @DisplayName("댓글 작성 후 갱신이상 확인")
    void writeComment3() {
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

        commentService.update(comment.getCommentId(), commentEdit);

        Post commentPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        assertEquals(1, commentPost.getComments().size());
        assertEquals("바뀐 댓글입니다", commentPost.getComments().get(0).getComment());
    }

    @Test
    @DisplayName("댓글 작성 후 삭제이상 확인")
    void writeComment4() {
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

        //when - 댓글이 삭제되었을 때 post 에 있는 comments 안에서도 삭제되어야 한다.
        commentService.delete(post.getPostId(), comment.getCommentId());

        Post commentPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        assertEquals(0, commentPost.getComments().size());

    }

    @Test
    @DisplayName("댓글 1개 조회")
    void getComment() {

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

        CommentResponse writtenComment = commentService.getComment(comment.getCommentId());

        Assertions.assertEquals("댓글입니다", writtenComment.getComment());
        assertEquals("댓글이 있는 글입니다", writtenComment.getPost().getTitle());
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
    @DisplayName("포스트에 맞는 댓글 리스트 삽입이상 확인")
    void getCommentListWherePostId1() {

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

        Comment addComment = Comment.builder()
                .post(post)
                .comment("새로 추가된 댓글입니다.")
                .build();

        commentRepository.save(addComment);

        Post addCommentPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        assertEquals(31, addCommentPost.getComments().size());

    }


    @Test
    @DisplayName("포스트에 맞는 댓글 리스트 갱신이상 확인")
    void getCommentListWherePostId2() {

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

        CommentEdit commentEdit = CommentEdit.builder()
                .comment("바뀔 내용입니다")
                .build();

        commentService.update(comments2.get(16).getCommentId(), commentEdit);
        commentService.update(comments.get(29).getCommentId(), commentEdit);

        List<Comment> commentList = postService.get(post.getPostId()).getComments();
        List<Comment> commentList2 = postService.get(post2.getPostId()).getComments();

        Assertions.assertEquals(30, commentList.size());
        Assertions.assertEquals("바뀔 내용입니다", commentList.get(29).getComment());

        Assertions.assertEquals(30, commentList2.size());
        Assertions.assertEquals("바뀔 내용입니다", commentList2.get(16).getComment());
    }


    @Test
    @DisplayName("포스트에 맞는 댓글 리스트 삭제이상 확인")
    void getCommentListWherePostId3() {

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

        commentService.delete(post.getPostId(),comments.get(1).getCommentId());

        List<Comment> commentList = postService.get(post.getPostId()).getComments();
        List<Comment> commentList2 = postService.get(post2.getPostId()).getComments();


        Assertions.assertEquals(29, commentList.size());

        Assertions.assertEquals(30, commentList2.size());
    }

    @Test
    @DisplayName("만약 포스트를 입력받지 못한경우 댓글 삽입을 할 수 없다")
    void failWriteCommentIfNotPutPost() {

        //todo

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
    @DisplayName("댓글리스트 중 댓글 한개 삭제")
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

        Comment comment2 = Comment.builder()
                .post(post)
                .comment("댓글입니다2")
                .writerId(24L)
                .build();

        commentRepository.save(comment);
        commentRepository.save(comment2);


        commentService.delete(post.getPostId(), comment2.getCommentId());


        assertEquals(1, commentRepository.count());

    }

    @Test
    @DisplayName("부모 객체에서 댓글 삭제")
    void deleteComment4() {

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


        commentRepository.saveAll(comments);

        //when repository 에서 가져와야 한다 , 왜냐하면 위에 post 객체는 repository 에 있는 객체가 아니기때문에 아무리 지워도 repository 안에서는 그대로다.
        Post deletePost = postRepository.findById(post.getPostId())
                .orElseThrow();

        deletePost.getComments().remove(6);

        assertEquals(29, deletePost.getComments().size());

    }


    @Test
    @DisplayName("댓글리스트 삭제 where PostId")
    @Transactional
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


    @Test
    @DisplayName("post 객체 삭제시 post 와 연관된 comments 전부 삭제")
    void deleteCommentListWhenPostDeleted() {
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

        assertEquals(60, commentRepository.count());

        postService.delete(post.getPostId());

        assertEquals(30, commentRepository.findAll().size());

    }

    @Test
    @DisplayName("추천을 1회 올리기")
    void upCommentRecommendation() {
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

        commentRepository.saveAll(comments);

        commentService.upRecommendation(comments.get(3).getCommentId());


        Post upPost = postRepository.findById(post.getPostId())
                .orElseThrow();

        Comment upComment = commentRepository.findById(comments.get(3).getCommentId())
                .orElseThrow();

        assertEquals(1, upComment.getRecommendation());
        assertEquals(1, upPost.getComments().get(3).getRecommendation());

    }


    @Test
    @DisplayName("비추천을 1회 올리기")
    void upCommentBad() {
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

        commentRepository.saveAll(comments);

        commentService.upBad(comments.get(3).getCommentId());


        Post badPost = postRepository.findById(post.getPostId())
                .orElseThrow();


        Comment badComment = commentRepository.findById(comments.get(3).getCommentId())
                .orElseThrow();

        assertEquals(1, badComment.getBad());
        assertEquals(1, badPost.getComments().get(3).getBad());

    }
}