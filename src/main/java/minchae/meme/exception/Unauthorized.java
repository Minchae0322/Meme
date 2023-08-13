package minchae.meme.exception;

public class Unauthorized extends RuntimeException{
    private static final String MESSAGE = "인증되지않은 사용자 입니다";
    public Unauthorized() {
        super(MESSAGE);
    }
}
