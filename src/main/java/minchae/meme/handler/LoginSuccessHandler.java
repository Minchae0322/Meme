package minchae.meme.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.entity.TokenInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Authorization", tokenInfo.getAccessToken());
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");
    }
}

