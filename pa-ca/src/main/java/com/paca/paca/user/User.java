package com.paca.paca.user;

import jakarta.persistence.*;

@Entity
@Table(name="`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public User () {}

    public User (long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    } 

    public Long getId () { return this.id; }
    public String getEmail () { return this.email; }
    public String getPassword () { return this.password; }
}
