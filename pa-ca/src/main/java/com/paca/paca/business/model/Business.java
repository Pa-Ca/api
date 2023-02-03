package com.paca.paca.business.model;
import com.paca.paca.business.tier.Tier;

import jakarta.persistence.*;

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
    private Long userId;
    @Column(name = "name")
    private String name;
    @Column(name = "verified")
    private Boolean verified;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_tier")
    private Tier tierId;


    public Business () {}

    public Business (Long id, Long userId, String name, Boolean verified, Tier tierId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.verified = verified;
        this.tierId = tierId;
    }

    // Getters
    public Long getId () { return this.id; }
    public Long getUserId () { return this.userId; }
    public String getName () { return this.name; }
    public Boolean getVerified () { return this.verified; }
    public Tier getTierId () { return this.tierId; }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setTierId(Tier tier) {
        this.tierId = tier;
    }
}