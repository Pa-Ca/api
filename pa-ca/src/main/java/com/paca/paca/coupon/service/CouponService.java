package com.paca.paca.coupon.service;

import com.paca.paca.coupon.dto.CouponDTO;
import com.paca.paca.coupon.model.Coupon;
import com.paca.paca.coupon.model.ProductCategoryCouponItem;
import com.paca.paca.coupon.model.ProductCouponItem;
import com.paca.paca.coupon.model.ProductSubCategoryCouponItem;
import com.paca.paca.coupon.repository.CouponRepository;
import com.paca.paca.coupon.repository.ProductCategoryCouponItemRepository;
import com.paca.paca.coupon.repository.ProductCouponItemRepository;
import com.paca.paca.coupon.repository.ProductSubCategoryCouponItemRepository;
import com.paca.paca.coupon.statics.CouponStatics;
import com.paca.paca.coupon.utils.CouponMapper;
import com.paca.paca.exception.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final ProductCategoryCouponItemRepository productCategoryCouponItemRepository;
    private final ProductCouponItemRepository productCouponItemRepository;
    private final ProductSubCategoryCouponItemRepository productSubCategoryCouponItemRepository;
    private final CouponMapper couponMapper;

    private CouponDTO couponByType(Coupon coupon) {
        switch (coupon.getType()) {
            case CouponStatics.Type.PRODUCT:
                return couponMapper.toDTOWithProductItems(coupon,
                        productCouponItemRepository.findAllByCoupon_Id(coupon.getId()));
            case CouponStatics.Type.CATEGORY:
                return couponMapper.toDTOWithProductCategoryItems(coupon,
                        productCategoryCouponItemRepository.findAllByCoupon_Id(coupon.getId()));
            case CouponStatics.Type.SUB_CATEGORY:
                return couponMapper.toDTOWithProductSubCategoryItems(coupon,
                        productSubCategoryCouponItemRepository.findAllByCoupon_Id(coupon.getId()));
            default:
                return null;
        }
    }

    public List<CouponDTO> getAll() {
        return couponRepository.findAll().stream().map(this::couponByType).collect(Collectors.toList());
    }

    public Page<CouponDTO> getAllByPage(Pageable pageable) {
        return couponRepository.findAll(pageable).map(this::couponByType);
    }

    public CouponDTO getById(Long id) {
        return couponByType(couponRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Coupon with id " + id + " does not exists", 300)));
    }

    public void delete (Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Coupon with id " + id + " does not exists", 300));

        switch (coupon.getType()) {
            case CouponStatics.Type.PRODUCT:
                productCategoryCouponItemRepository.deleteAllByCoupon_Id(id);
                break;
            case CouponStatics.Type.CATEGORY:
                productCouponItemRepository.deleteAllByCoupon_Id(id);
                break;
            case CouponStatics.Type.SUB_CATEGORY:
                productSubCategoryCouponItemRepository.deleteAllByCoupon_Id(id);
                break;
            default:
                throw new NotFoundException(
                        "Coupon with id " + id + " does not have a valid type. Found: " + coupon.getType(), 300);
        }
        couponRepository.deleteById(id);
    }

}
