package com.imogo.imogo_backend.model;

import com.imogo.imogo_backend.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    // @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private String role;

    private boolean fromSocial;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // implementar se quiser lógica real
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // implementar se quiser lógica real
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // implementar se quiser lógica real
    }

    @Override
    public boolean isEnabled() {
        return true; // implementar se quiser lógica real
    }
}
