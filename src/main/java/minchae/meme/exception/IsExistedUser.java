package minchae.meme.exception;

public class IsExistedUser extends RuntimeException{

    private static final String MESSAGE = "이미 등록된 사용자 입니다.";
    public IsExistedUser() {
        super(MESSAGE);
    }
}
