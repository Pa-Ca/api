package com.paca.paca.branch.model;

import com.paca.paca.sale.model.Tax;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "tax")
public class DefaultTax {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_tax_seq")
    @SequenceGenerator(name = "default_tax_seq", sequenceName = "default_tax_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tax_id", nullable = false)
    private Tax tax;
};
