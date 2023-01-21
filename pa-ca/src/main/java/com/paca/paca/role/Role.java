package com.paca.paca.role;
import com.paca.paca.statics.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    //@SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private UserRole name;

    public Role() {}
    public Role(UserRole name) { this.name = name; }
    public Role(Long id, UserRole name) { this.id = id; this.name = name; }

    public Role(String name) { this.name = UserRole.valueOf(name); }
    public Role(Long id, String name) { this.id = id; this.name = UserRole.valueOf(name); }

    //Getters
    public Long getId() { return id; }
    public UserRole getName() { return name; }

    // Setters
    public void setName(UserRole name) { this.name = name; }
}
