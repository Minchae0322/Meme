package minchae.meme.config;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.exception.PostNotFound;
import minchae.meme.repository.PostRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
public class PostPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (targetType.equals("POST")) {
            User user = (User) authentication.getPrincipal();
            Post post = postRepository.findById((Long) targetId)
                    .orElseThrow(PostNotFound::new);
            return Objects.equals(user.getId(), post.getAuthor().getId());
        }

        return false;
    }
}
