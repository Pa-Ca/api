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
    @Column(name = "reservation_limit")
    private int reservation_limit;
    @Column(name = "tier_cost")
    private float tier_cost;

    public Tier() {}
    public Tier(BusinessTier name, int reservation_limit, float tier_cost) { 
        this.name = name;
        this.reservation_limit = reservation_limit;
        this.tier_cost = tier_cost;
    }

    public Tier(Long id, BusinessTier name, int reservation_limit, float tier_cost) { this.id = id; this.name = name; }

    public Tier(String name) { this.name = BusinessTier.valueOf(name); }
    public Tier(Long id, String name) { this.id = id; this.name = BusinessTier.valueOf(name); }

    //Getters
    public Long getId() { return id; }
    public BusinessTier getName() { return name; }
    public int getReservationLimit() { return reservation_limit; }
    public float getTierCost() { return tier_cost; }

    // Setters
    public void setName(BusinessTier name) { this.name = name; }
    public void setReservationLimit(int reservation_limit) { this.reservation_limit = reservation_limit; }
    public void setTierCost(float tier_cost) { this.tier_cost = tier_cost; }
}