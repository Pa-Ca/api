package com.paca.paca.coupon.utils;

import com.paca.paca.coupon.dto.CouponDTO;
import com.paca.paca.coupon.dto.CouponItemDTO;
import com.paca.paca.coupon.model.Coupon;
import com.paca.paca.coupon.model.ProductCategoryCouponItem;
import com.paca.paca.coupon.model.ProductCouponItem;
import com.paca.paca.coupon.model.ProductSubCategoryCouponItem;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = CouponItemMapper.class)
public interface CouponMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(target = "items", qualifiedByName = "toProductCouponItemDTOList")
    CouponDTO toDTOWithProductItems(Coupon coupon, List<ProductCouponItem> productCouponItems);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(target = "items", qualifiedByName = "toProductCategoryCouponItemDTOList")
    CouponDTO toDTOWithProductCategoryItems(Coupon coupon, List<ProductCategoryCouponItem> productCategoryCouponItems);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(target = "items", qualifiedByName = "toProductSubCategoryCouponItemDTOList")
    CouponDTO toDTOWithProductSubCategoryItems(Coupon coupon, List<ProductSubCategoryCouponItem> productSubCategoryCouponItems);

    @Named("toProductCouponItemDTOList")
    default List<CouponItemDTO> toProductCouponItemDTOList(List<ProductCouponItem> productCouponItems) {
        if (productCouponItems == null) {
            return null;
        }

        List<CouponItemDTO> list = new ArrayList<>(productCouponItems.size());
        for (ProductCouponItem productCouponItem : productCouponItems) {
            list.add(Mappers.getMapper(CouponItemMapper.class).toProductCouponItemDTO(productCouponItem));
        }

        return list;
    }

    @Named("toProductCategoryCouponItemDTOList")
    default List<CouponItemDTO> toProductCategoryCouponItemDTOList(List<ProductCategoryCouponItem> productCategoryCouponItems) {
        if (productCategoryCouponItems == null) {
            return null;
        }

        List<CouponItemDTO> list = new ArrayList<>(productCategoryCouponItems.size());
        for (ProductCategoryCouponItem productCategoryCouponItem : productCategoryCouponItems) {
            list.add(Mappers.getMapper(CouponItemMapper.class).toProductCategoryCouponItemDTO(productCategoryCouponItem));
        }

        return list;
    }

    @Named("toProductCategoryCouponItemDTOList")
    default List<CouponItemDTO> toProductSubCategoryCouponItemDTOList(List<ProductSubCategoryCouponItem> productSubCategoryCouponItems) {
        if (productSubCategoryCouponItems == null) {
            return null;
        }

        List<CouponItemDTO> list = new ArrayList<>(productSubCategoryCouponItems.size());
        for (ProductSubCategoryCouponItem productSubCategoryCouponItem : productSubCategoryCouponItems) {
            list.add(Mappers.getMapper(CouponItemMapper.class).toProductSubCategoryCouponItemDTO(productSubCategoryCouponItem));
        }

        return list;
    }
}

