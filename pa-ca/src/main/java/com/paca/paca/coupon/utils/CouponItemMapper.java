package com.paca.paca.coupon.utils;

import com.paca.paca.coupon.dto.CouponItemDTO;
import com.paca.paca.coupon.model.ProductCategoryCouponItem;
import com.paca.paca.coupon.model.ProductCouponItem;
import com.paca.paca.coupon.model.ProductSubCategoryCouponItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponItemMapper {

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.name", target = "name")
    @Mapping(target = "type", constant = "Product")
    CouponItemDTO toProductCouponItemDTO(ProductCouponItem productCouponItem);

    @Mapping(source = "productCategory.id", target = "id")
    @Mapping(source = "productCategory.name", target = "name")
    @Mapping(target = "type", constant = "ProductCategory")
    CouponItemDTO toProductCategoryCouponItemDTO(ProductCategoryCouponItem productCategoryCouponItem);

    @Mapping(source = "productSubCategory.id", target = "id")
    @Mapping(source = "productSubCategory.name", target = "name")
    @Mapping(target = "type", constant = "ProductSubCategory")
    CouponItemDTO toProductSubCategoryCouponItemDTO(ProductSubCategoryCouponItem productSubCategoryCouponItem);
}
