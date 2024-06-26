package com.paca.paca.branch.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "payment_option")
public class PaymentOption {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_option_seq")
    @SequenceGenerator(name = "payment_option_seq", sequenceName = "payment_option_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
};



