package com.paca.paca.business.tier;
import com.paca.paca.statics.BusinessTier;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tier")
public class Tier {
    @Id
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

    public Tier(BusinessTier name) {
        this.id = (long) name.ordinal();
        this.name = name;
    }

    public Tier(Long id, BusinessTier name) {
        this.id = id;
        this.name = name;
    }

    public Tier(Long id, String name) {
        this.id = id;
        this.name = BusinessTier.valueOf(name);
    }

}