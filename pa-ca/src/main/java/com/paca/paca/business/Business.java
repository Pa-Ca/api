package com.paca.paca.business;
import com.paca.paca.tier.Tier;
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
    private Long user_id;
    @Column(name = "name")
    private String name;
    @Column(name = "verified")
    private Boolean verified;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_tier")
    private Tier tier_id;


    public Business () {}

    public Business (Long id, Long user_id, String name, Boolean verified, Tier tier_id) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.verified = verified;
        this.tier_id = tier_id;
    }

    // Getters
    public Long getId () { return this.id; }
    public Long user_id () { return this.user_id; }
    public String getName () { return this.name; }
    public Boolean getVerified () { return this.verified; }
    public Tier getTierId () { return this.tier_id; }

    // Setters
    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setTierId(Tier tier) {
        this.tier_id = tier;
    }
}