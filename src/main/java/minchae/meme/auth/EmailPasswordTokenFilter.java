package minchae.meme.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class EmailPasswordTokenFilter extends AbstractAuthenticationProcessingFilter {

    public EmailPasswordTokenFilter() {
        super("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        EmailPassword emailPassword = new ObjectMapper().readValue(request.getInputStream(), EmailPassword.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(
                emailPassword.email,
                emailPassword.password
        );

        usernamePasswordAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Getter
    static class EmailPassword {
        private String email;
        private String password;
    }

}

