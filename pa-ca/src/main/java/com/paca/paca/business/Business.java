package com.paca.paca.business;

import jakarta.persistence.*;

@Entity
@Table(name="`business`")
public class Business {
    @Id
    @SequenceGenerator(
        name = "business_sequence",
        sequenceName = "business_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "business_sequence"    
    )
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // private Long user_id;
    private String name;
    private Boolean verified;
    // private Long tier_id;


    public Business () {}

    public Business (Long id, String name, Boolean verified) {
        this.id = id;
        this.name = name;
        this.verified = verified;
    }

    public Long getId () { return this.id; }
    public String getName () { return this.name; }
    public Boolean getVerified () { return this.verified; }
}