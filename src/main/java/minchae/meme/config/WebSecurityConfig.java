package minchae.meme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import minchae.meme.auth.EmailPasswordTokenFilter;
import minchae.meme.entity.enumClass.Authorization;
import minchae.meme.handler.LoginFailHandler;
import minchae.meme.repository.UserRepository;
import minchae.meme.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return null;
    }

    @Bean
    public EmailPasswordTokenFilter emailPasswordTokenFilter() {
        EmailPasswordTokenFilter filter = new EmailPasswordTokenFilter();
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/board/"));
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
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(emailPasswordTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/user/posts").hasAuthority("USER")
                        .requestMatchers("/").hasAnyAuthority("USER", "ADMIN", "MANAGER"))
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
