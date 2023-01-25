package com.paca.paca.tier;
import com.paca.paca.statics.BusinessTier;
import jakarta.persistence.*;

@Entity
@Table(name = "tier")
public class Tier {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tier_seq")
    //@SequenceGenerator(name = "tier_seq", sequenceName = "tier_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private BusinessTier name;

    // private int reservation_limit
    // private int tier_cost

    public Tier() {}
    public Tier(BusinessTier name) { this.name = name; }
    public Tier(Long id, BusinessTier name) { this.id = id; this.name = name; }

    public Tier(String name) { this.name = BusinessTier.valueOf(name); }
    public Tier(Long id, String name) { this.id = id; this.name = BusinessTier.valueOf(name); }

    //Getters
    public Long getId() { return id; }
    public BusinessTier getName() { return name; }

    // Setters
    public void setName(BusinessTier name) { this.name = name; }
}