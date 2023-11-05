package com.paca.paca.coupon.repository;

import com.paca.paca.coupon.model.ProductSubCategoryCouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSubCategoryCouponItemRepository extends JpaRepository<ProductSubCategoryCouponItem, Long> {
}
