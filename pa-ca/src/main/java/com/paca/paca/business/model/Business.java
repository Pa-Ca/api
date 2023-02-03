package com.paca.paca.business.model;
import com.paca.paca.business.tier.Tier;

import com.paca.paca.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;

@Builder
@Entity
@Table(name="`business`")
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
    @JoinColumn(name = "tier_id")
    private Tier tier;

}
