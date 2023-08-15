package com.paca.paca.business.model;

import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;

import com.paca.paca.statics.BusinessTier;

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
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private BusinessTier name;

    @Column(name = "reservation_limit", nullable = false)
    private int reservationLimit;

    @Column(name = "tier_cost", nullable = false)
    private BigDecimal tierCost;

}