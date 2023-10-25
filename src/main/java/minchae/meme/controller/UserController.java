package minchae.meme.controller;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.User;
import minchae.meme.request.UserEdit;
import minchae.meme.response.UserInfoResponse;
import minchae.meme.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/information")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MANAGER')")
    public UserInfoResponse userInfo(@AuthenticationPrincipal User user) {
        return UserInfoResponse.builder().build().userToUserInfo(user);
    }

    @PostMapping("/user/information/isValidNickname")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MANAGER')")
    public boolean isValidNickname(@RequestBody UserEdit userEdit) {
        return userService.isValidNickname(userEdit.getNickname());
    }

    @PostMapping("/user/information/changeNickname")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'MANAGER')")
    public void changeNickName(@RequestBody UserEdit userEdit, @AuthenticationPrincipal User user) {
        userService.changeNickName(user.getId(), userEdit.getNickname());

    }
}
