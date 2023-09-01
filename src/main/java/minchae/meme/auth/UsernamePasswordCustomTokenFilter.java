package minchae.meme.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class UsernamePasswordCustomTokenFilter extends AbstractAuthenticationProcessingFilter {

    public UsernamePasswordCustomTokenFilter() {
        super("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getMethod().equals("OPTIONS")) {
            return this.getAuthenticationManager().authenticate(null);
        }

        EmailPassword emailPassword = new ObjectMapper().readValue(request.getInputStream(), EmailPassword.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(
                emailPassword.username,
                emailPassword.password
        );


        usernamePasswordAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }


    @Getter
    static class EmailPassword {
        private String username;
        private String password;
    }

}

