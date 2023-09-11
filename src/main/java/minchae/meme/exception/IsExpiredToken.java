package minchae.meme.exception;

public class IsExpiredToken extends RuntimeException{
    private static final String MESSAGE = "토큰의 기한이 지났습니다.";
    public IsExpiredToken() {
        super(MESSAGE);
    }
}
