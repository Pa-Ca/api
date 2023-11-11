package com.paca.paca.coupon.repository;

import com.paca.paca.coupon.model.ProductCategoryCouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryCouponItemRepository extends JpaRepository<ProductCategoryCouponItem, Long> {
    List<ProductCategoryCouponItem> findAllByCoupon_Id(Long id);

    void deleteAllByCoupon_Id(Long id);
}
