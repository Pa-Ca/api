package com.paca.paca.sale.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale_tax")
public class SaleTax {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_tax_seq")
    @SequenceGenerator(name = "sale_tax_seq", sequenceName = "sale_tax_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @OneToOne(optional = false)
    @JoinColumn(name = "tax_id", nullable = false)
    private Tax tax;
}
