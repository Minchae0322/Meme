package minchae.meme.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import minchae.meme.auth.provider.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> list = List.of(
                "/auth",
                "/home",
                "/board/posts");

        /*if (request.getMethod().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");

            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
            return;
        }*/

        String url = String.valueOf(request.getRequestURL());
        for (String s : list) {
            if (url.contains(s)) {
                filterChain.doFilter(request, response);
                return;
            }
        }


        String token = resolveToken(request);



            // 2. validateToken 으로 토큰 유효성 검사
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            //response.sendRedirect("/auth/login");



    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }
}
