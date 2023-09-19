package minchae.meme.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class MailAuthentication {
    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;

    private String verificationCode;

    private Date createdTime = new Date();

    @Builder
    public MailAuthentication(Long id, String email, String verificationCode) {
        this.id = id;
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
