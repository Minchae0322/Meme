package minchae.meme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import minchae.meme.auth.UsernamePasswordCustomTokenFilter;
import minchae.meme.auth.JwtAuthenticationFilter;
import minchae.meme.auth.provider.JwtTokenProvider;
import minchae.meme.handler.LoginFailHandler;
import minchae.meme.handler.LoginSuccessHandler;
import minchae.meme.repository.UserRepository;
import minchae.meme.service.impl.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class WebSecurityConfig {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return null;
    }

    @Bean
    public UsernamePasswordCustomTokenFilter emailPasswordTokenFilter() {
        UsernamePasswordCustomTokenFilter filter = new UsernamePasswordCustomTokenFilter();
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtTokenProvider));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(new ObjectMapper()));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setAuthenticationManager(authenticationManage());
        return filter;
    }



    @Bean
    public AuthenticationManager authenticationManage() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new UserDetailServiceImpl(userRepository, getPasswordEncoder()));
        provider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://mememmm.com");
        configuration.addAllowedOrigin("http://13.125.165.102");
        configuration.addAllowedOrigin("https://www.youtube.com");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //.addFilterBefore(emailPasswordTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/board/user/writePost").hasAnyAuthority("USER", "ADMIN", "MANAGER")
                        .anyRequest().permitAll()
                        )
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/home"))
               /* .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .failureUrl("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("username")
                        .passwordParameter("password"))*/
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(86400))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

}
