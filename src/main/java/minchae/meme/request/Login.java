package minchae.meme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Login {

    private String username;
    private String password;

    @Builder
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
