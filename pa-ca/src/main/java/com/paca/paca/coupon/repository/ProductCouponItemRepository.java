package com.paca.paca.coupon.repository;

import com.paca.paca.coupon.model.ProductCouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCouponItemRepository extends JpaRepository<ProductCouponItem, Long> {
    List<ProductCouponItem> findAllByCoupon_Id(Long id);

    void deleteAllByCoupon_Id(Long id);
}
