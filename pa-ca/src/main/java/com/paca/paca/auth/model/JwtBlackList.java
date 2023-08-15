package com.paca.paca.auth.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jwt_black_list")
public class JwtBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_black_list_seq")
    @SequenceGenerator(name = "jwt_black_list_seq", sequenceName = "jwt_black_list_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration", nullable = false)
    private Date expiration;
}