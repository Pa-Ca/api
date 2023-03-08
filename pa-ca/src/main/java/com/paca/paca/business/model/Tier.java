package com.paca.paca.business.model;
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
    private int reservationLimit;
    @Column(name = "tier_cost")
    private float tierCost;

}