package com.paca.paca.business.model;

import com.paca.paca.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import com.paca.paca.business.tier.Tier;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_seq")
    @SequenceGenerator(name = "business_seq", sequenceName = "business_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "name")
    private String name;
    @Column(name = "verified")
    private Boolean verified;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_tier")
    private Tier tierId;

}