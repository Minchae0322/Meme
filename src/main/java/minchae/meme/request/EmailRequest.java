package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private String email;

    private String verificationCode;

    @Builder
    public EmailRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
