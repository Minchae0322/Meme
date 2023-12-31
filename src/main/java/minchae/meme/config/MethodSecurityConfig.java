package minchae.meme.config;

import lombok.RequiredArgsConstructor;
import minchae.meme.repository.CommentRepository;
import minchae.meme.repository.PostRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class MethodSecurityConfig {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator(postRepository, commentRepository));
        return handler;
    }
}
