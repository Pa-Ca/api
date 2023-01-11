package com.paca.paca.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id

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
