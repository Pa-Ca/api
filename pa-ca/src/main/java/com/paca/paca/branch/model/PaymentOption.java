package com.paca.paca.branch.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "payment_option")
public class PaymentOption {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_option_seq")
    @SequenceGenerator(name = "payment_option_seq", sequenceName = "payment_option_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;
};
