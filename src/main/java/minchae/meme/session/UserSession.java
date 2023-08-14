package minchae.meme.session;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.User;

@Getter
@Setter
@RequiredArgsConstructor
public class UserSession {

    private Long id;

    @Builder
    public UserSession(Long id) {
        this.id = id;
    }
}
