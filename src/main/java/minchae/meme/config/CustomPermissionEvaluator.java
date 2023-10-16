package minchae.meme.config;

import lombok.RequiredArgsConstructor;
import minchae.meme.entity.Comment;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.exception.CommentNotFound;
import minchae.meme.exception.PostNotFound;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
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

        if (targetType.equals("COMMENT")) {
            User user = (User) authentication.getPrincipal();
            Comment comment = commentRepository.findById((Long) targetId)
                    .orElseThrow(CommentNotFound::new);
            return Objects.equals(user.getId(), comment.getUser().getId());
        }



        return false;
    }
}
