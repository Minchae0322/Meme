package minchae.meme.exception;

public class CommentNotFound extends RuntimeException{

    private static final String MESSAGE = "존재하지않는 댓글입니다";

    public CommentNotFound() {
        super(MESSAGE);
    }
}
