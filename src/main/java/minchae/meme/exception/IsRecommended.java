package minchae.meme.exception;

public class IsRecommended extends RuntimeException{
    private static final String MESSAGE = "이미 추천을 눌렀습니다.";
    public IsRecommended() {
        super(MESSAGE);
    }
}
