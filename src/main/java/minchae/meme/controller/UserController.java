package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.User;
import minchae.meme.response.UserInfoResponse;
import minchae.meme.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/information")
    @PreAuthorize("hasAuthority('USER')")
    public UserInfoResponse userInfo(@AuthenticationPrincipal User user) {
        return UserInfoResponse.builder().build().userToUserInfo(user);
    }

    @PostMapping("/user/information/changeName")

    public void changeNickName(Long userId, String nickName) {
        userService.changeNickName(userId, nickName);

    }
}
