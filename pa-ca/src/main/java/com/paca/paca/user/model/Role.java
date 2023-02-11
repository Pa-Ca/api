package com.paca.paca.user.model;

import com.paca.paca.statics.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private UserRole name;

    public Role(UserRole name) {
        this.id = (long) name.ordinal();
        this.name = name;
    }

    public Role(Long id, UserRole name) {
        this.id = id;
        this.name = name;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = UserRole.valueOf(name);
    }
}
