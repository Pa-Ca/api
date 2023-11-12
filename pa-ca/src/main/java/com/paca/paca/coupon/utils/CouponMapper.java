package com.paca.paca.coupon.utils;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.coupon.dto.CouponDTO;
import com.paca.paca.coupon.dto.CouponItemDTO;
import com.paca.paca.coupon.model.Coupon;
import com.paca.paca.coupon.model.ProductCategoryCouponItem;
import com.paca.paca.coupon.model.ProductCouponItem;
import com.paca.paca.coupon.model.ProductSubCategoryCouponItem;
import com.paca.paca.product.model.Product;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(source = "productCouponItems", target = "items", qualifiedByName = "toProductCouponItemDTOList")
    CouponDTO toDTOWithProductItems(Coupon coupon, List<ProductCouponItem> productCouponItems);

    @Mapping(source = "productCategoryCouponItems", target = "items", qualifiedByName = "toProductCategoryCouponItemDTOList")
    CouponDTO toDTOWithProductCategoryItems(Coupon coupon, List<ProductCategoryCouponItem> productCategoryCouponItems);

    @Mapping(source = "productSubCategoryCouponItems", target = "items", qualifiedByName = "toProductSubCategoryCouponItemDTOList")
    CouponDTO toDTOWithProductSubCategoryItems(Coupon coupon, List<ProductSubCategoryCouponItem> productSubCategoryCouponItems);

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

    @Named("toProductCouponItemDTOList")
    default List<CouponItemDTO> toProductCouponItemDTOList(List<ProductCouponItem> productCouponItems) {
        if (productCouponItems == null) {
            return null;
        }

        List<CouponItemDTO> list = new ArrayList<>(productCouponItems.size());
        for (ProductCouponItem productCouponItem : productCouponItems) {
            list.add(toProductCouponItemDTO(productCouponItem));
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
            list.add(toProductCategoryCouponItemDTO(productCategoryCouponItem));
        }

        return list;
    }

    @Named("toProductSubCategoryCouponItemDTOList")
    default List<CouponItemDTO> toProductSubCategoryCouponItemDTOList(List<ProductSubCategoryCouponItem> productSubCategoryCouponItems) {
        if (productSubCategoryCouponItems == null) {
            return null;
        }

        List<CouponItemDTO> list = new ArrayList<>(productSubCategoryCouponItems.size());
        for (ProductSubCategoryCouponItem productSubCategoryCouponItem : productSubCategoryCouponItems) {
            list.add(toProductSubCategoryCouponItemDTO(productSubCategoryCouponItem));
        }

        return list;
    }

    Coupon toEntity(CouponDTO dto);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "coupon", ignore = true)
    ProductCouponItem toProductItemEntity(CouponItemDTO dto);
    default ProductCouponItem toProductItemEntity(CouponItemDTO dto, Product product, Coupon coupon) {
        ProductCouponItem item = toProductItemEntity(dto);
        item.setProduct(product);
        item.setCoupon(coupon);
        return item;
    };

    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "coupon", ignore = true)
    @Mapping(target = "branch", ignore = true)
    ProductCategoryCouponItem toProductCategoryItemEntity(CouponItemDTO dto);
    default ProductCategoryCouponItem toProductCategoryItemEntity(
            CouponItemDTO dto, ProductCategory category, Coupon coupon, Branch branch) {
        ProductCategoryCouponItem item = toProductCategoryItemEntity(dto);
        item.setProductCategory(category);
        item.setCoupon(coupon);
        item.setBranch(branch);
        return item;
    };

    @Mapping(target = "productSubCategory", ignore = true)
    @Mapping(target = "coupon", ignore = true)
    ProductSubCategoryCouponItem toProductSubCategoryItemEntity(CouponItemDTO dto);
    default ProductSubCategoryCouponItem toProductSubCategoryItemEntity(
            CouponItemDTO dto, ProductSubCategory subCategory, Coupon coupon) {
        ProductSubCategoryCouponItem item = toProductSubCategoryItemEntity(dto);
        item.setProductSubCategory(subCategory);
        item.setCoupon(coupon);
        return item;
    };

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Coupon update(CouponDTO dto, @MappingTarget Coupon coupon);

}

