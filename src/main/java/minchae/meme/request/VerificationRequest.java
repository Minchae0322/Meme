package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    private String subject;
    private String verificationCode;

    @Builder
    public VerificationRequest(String subject, String verificationCode) {
        this.subject = subject;
        this.verificationCode = verificationCode;
    }
}
