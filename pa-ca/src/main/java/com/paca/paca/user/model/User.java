package com.paca.paca.user.model;

import java.util.List;
import java.util.Collection;

import com.paca.paca.statics.AuthProvider;

import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.verified = false;
        this.role = role;
        this.provider = AuthProvider.paca.name();
    }

    public User(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = false;
        this.role = role;
        this.provider = AuthProvider.paca.name();
    }

    public User(Long id, String email, String password, Role role, String provider, String providerId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = false;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getName().name()));
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
