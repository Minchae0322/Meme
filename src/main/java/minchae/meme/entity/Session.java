package minchae.meme.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @UniqueElements
    private String accessToken;

    @ManyToOne
    private User user;

    @Builder
    public Session(Long id, String accessToken, User user) {
        this.id = id;
        this.accessToken = accessToken;
        this.user = user;
    }
}
