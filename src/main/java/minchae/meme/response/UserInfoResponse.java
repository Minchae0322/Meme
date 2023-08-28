package minchae.meme.response;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import minchae.meme.entity.User;

@RequiredArgsConstructor
@Getter
@Setter
public class UserInfoResponse {

    private String username;

    @Email
    private String email;

    private String nickName;

    private Boolean enable;

    @Builder
    public UserInfoResponse(String username, String email, String nickName, Boolean enable) {
        this.username = username;
        this.email = email;
        this.nickName = nickName;
        this.enable = enable;
    }

    public UserInfoResponse userToUserInfo(User user) {

        return UserInfoResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .enable(user.getEnable())
                .build();
    }
}

