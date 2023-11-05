package com.paca.paca.coupon.model;

import com.paca.paca.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "`product_coupon_item`")
public class ProductCouponItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_coupon_item_seq")
    @SequenceGenerator(name = "product_coupon_item_seq", sequenceName = "product_coupon_item_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
}
