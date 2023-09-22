package minchae.meme.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;

    private String verificationSubject;

    private String verificationCode;

    private Date createdTime = new Date();

    @Builder
    public VerificationToken(Long id, String verificationSubject, String verificationCode) {
        this.id = id;
        this.verificationSubject = verificationSubject;
        this.verificationCode = verificationCode;
    }
}
