package com.paca.paca.product.model;

import java.math.BigDecimal;

import com.paca.paca.productSubCategory.model.ProductSubCategory;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_sub_category_id", nullable = false)
    private ProductSubCategory subCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @Column(name = "image", nullable = false)
    private String image;
}
