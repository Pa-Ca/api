package com.paca.paca.coupon.repository;

import com.paca.paca.coupon.model.ProductCouponItem;
import com.paca.paca.coupon.model.ProductSubCategoryCouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSubCategoryCouponItemRepository extends JpaRepository<ProductSubCategoryCouponItem, Long> {
    List<ProductSubCategoryCouponItem> findAllByCoupon_Id(Long id);

    void deleteAllByCoupon_Id(Long id);
}
