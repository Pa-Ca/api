package com.paca.paca.branch.model;

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
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "value")
    private Float value;
};



