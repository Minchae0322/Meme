package minchae.meme.exception;

public class IsWrongIdAndPassword extends RuntimeException {
    private static final String MESSAGE = "아이디 또는 비밀번호가 틀렸습니다.";


    public IsWrongIdAndPassword() {
        super(MESSAGE);

    }
}
