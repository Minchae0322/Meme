package minchae.meme.exception;

public class IsNotExistUser extends RuntimeException{
    private static final String MESSAGE = "아이디와 패스워드를 확인해 주세요";
    public IsNotExistUser() {
        super(MESSAGE);
    }
}
