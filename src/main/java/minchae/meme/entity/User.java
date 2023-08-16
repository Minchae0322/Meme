package minchae.meme.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import minchae.meme.auth.CustomAuthorityDeserializer;
import minchae.meme.entity.enumClass.Authorization;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@RequiredArgsConstructor
@Getter
@Setter

public class User implements UserDetails {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column
    private LocalDateTime createdDate;

    @JsonSerialize
    @JsonDeserialize
    @Enumerated(EnumType.STRING)
    private Authorization authorizations;

    private Boolean enable;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;


    @Builder
    public User(Long id, String name, String password, String email, Authorization authorizations, Boolean enable) {
        this.id = id;
        this.username = name;
        this.password = password;
        this.email = email;
        this.createdDate = LocalDateTime.now();
        this.authorizations = authorizations;
        this.enable = enable;
    }

    @Override
    //@JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @JsonDeserialize
    @JsonSerialize
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(authorizations.name()));
        return auth;
    }

    @Override
    public java.lang.String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
