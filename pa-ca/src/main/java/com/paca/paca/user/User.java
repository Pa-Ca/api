package com.paca.paca.user;

import com.paca.paca.role.Role;
import com.paca.paca.statics.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {
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
    @ManyToOne(fetch = FetchType.EAGER , optional = false)
    @JoinColumn(name = "role_id")
    private Role roleId;

    public User () {}

    public User (String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.verified = false;
        this.loggedIn = false;
        this.roleId = role;
    }

    public User (Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = false;
        this.loggedIn = false;
        this.roleId = role;
    }

    // Getters
    public Long getId () { return this.id; }
    public String getEmail () { return this.email; }
    public String getPassword () { return this.password; }
    public Boolean isVerified() { return this.verified; }
    public Boolean isLoggedIn() { return this.loggedIn; }
    public Role getRoleId() { return this.roleId; }

    // Setters
    public void setEmail (String email) { this.email = email; }
    public void setPassword (String password) { this.password = password; }
    public void setVerification(Boolean verification) { this.verified = verification; }
    public void setLogin(Boolean login) { this.loggedIn = login; }
    public void setRoleId(Role role) { this.roleId = role; }
}
