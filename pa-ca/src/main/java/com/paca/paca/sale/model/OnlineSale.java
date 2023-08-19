package com.paca.paca.sale.model;

import lombok.*;

import jakarta.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "online_sale")
public class OnlineSale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "online_sale_seq")
    @SequenceGenerator(name = "online_sale_seq", sequenceName = "online_sale_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;
}
