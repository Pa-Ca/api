package com.paca.paca.user.model;

import java.util.Collection;
import java.util.List;

import com.paca.paca.statics.AuthProvider;
import com.paca.paca.user.statics.UserStatics;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "logged_in")
    private Boolean loggedIn;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "provider")
    private String provider;

    @Column(name = "registration_status")
    private int registrationStatus;

    @Column(name = "provider_id")
    private String provider_id;

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.verified = false;
        this.loggedIn = false;
        this.role = role;
        this.provider = AuthProvider.paca.name();
        this.registrationStatus = UserStatics.RegistrationStatus.unregistered;
    }

    public User(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = false;
        this.loggedIn = false;
        this.role = role;
        this.provider = AuthProvider.paca.name();
        this.registrationStatus = UserStatics.RegistrationStatus.unregistered;
    }

    public User(Long id, String email, String password, Role role, String provider, String provider_id) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = false;
        this.loggedIn = false;
        this.role = role;
        this.provider = provider;
        this.provider_id = provider_id;
        this.registrationStatus = UserStatics.RegistrationStatus.unregistered;
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
