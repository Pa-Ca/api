package com.paca.paca.sale.model;

import java.math.BigDecimal;

import com.paca.paca.product.model.Product;

import lombok.*;

import jakarta.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale_product")
public class SaleProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_product_seq")
    @SequenceGenerator(name = "sale_product_seq", sequenceName = "sale_product_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "ammount")
    private Integer ammount;

    @Column(name = "price")
    private BigDecimal price;
}
