package com.paca.paca.productSubCategory.model;

import com.paca.paca.branch.model.Branch;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_sub_category")
public class ProductSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sub_category_seq")
    @SequenceGenerator(name = "product_sub_category_seq", sequenceName = "product_sub_category_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory category;

    @Column(name = "name", nullable = false)
    private String name;
}