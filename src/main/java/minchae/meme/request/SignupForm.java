package minchae.meme.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignupForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;


    @NotBlank
    private String phoneNum;

    @Builder
    public SignupForm(String name, String password, String email, String phoneNum) {
        this.username = name;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }
}
