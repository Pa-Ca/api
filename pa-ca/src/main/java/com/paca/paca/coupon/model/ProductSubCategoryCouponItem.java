package com.paca.paca.coupon.model;

import com.paca.paca.productSubCategory.model.ProductSubCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "`product_sub_category_coupon_item`")
public class ProductSubCategoryCouponItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sub_category_coupon_item_seq")
    @SequenceGenerator(name = "product_sub_category_coupon_item_seq",
            sequenceName = "product_sub_category_coupon_item_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_sub_category_id", nullable = false)
    private ProductSubCategory productSubCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
}
