package minchae.meme.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserSession {

    private Long id;

    private String name;

    private String accessToken;

    @ManyToOne
    private User user;

    @Builder
    public UserSession(Long id, String name, String accessToken, User user) {
        this.id = id;
        this.name = name;
        this.accessToken = accessToken;
        this.user = user;
    }
}
