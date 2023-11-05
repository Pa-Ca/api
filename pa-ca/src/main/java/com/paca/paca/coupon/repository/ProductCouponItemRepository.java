package com.paca.paca.coupon.repository;

import com.paca.paca.coupon.model.ProductCouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCouponItemRepository extends JpaRepository<ProductCouponItem, Long> {
}
