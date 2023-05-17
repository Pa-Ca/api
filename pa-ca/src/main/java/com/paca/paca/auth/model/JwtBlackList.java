package com.paca.paca.auth.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jwt_black_list")
public class JwtBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_black_list_seq")
    @SequenceGenerator(name = "jwt_black_list_seq", sequenceName = "jwt_black_list_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;
}