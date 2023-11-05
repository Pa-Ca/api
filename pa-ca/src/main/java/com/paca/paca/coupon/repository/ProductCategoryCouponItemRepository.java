package com.paca.paca.coupon.repository;

import com.paca.paca.coupon.model.ProductCategoryCouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryCouponItemRepository extends JpaRepository<ProductCategoryCouponItem, Long> {
}
