package com.paca.paca.coupon.model;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.productSubCategory.model.ProductCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@Entity
@Table (name = "product_category_coupon_item")
public class ProductCategoryCouponItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_coupon_item_seq")
    @SequenceGenerator(name = "product_category_coupon_item_seq", sequenceName = "product_category_coupon_item_seq",
            allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
}
