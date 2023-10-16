package minchae.meme.request;


import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserEdit {
    private String nickname;


    @Builder
    public UserEdit(String nickname) {
        this.nickname = nickname;
    }
}
