package com.paca.paca.coupon.service;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.coupon.dto.CouponDTO;
import com.paca.paca.coupon.model.Coupon;
import com.paca.paca.coupon.repository.CouponRepository;
import com.paca.paca.coupon.repository.ProductCategoryCouponItemRepository;
import com.paca.paca.coupon.repository.ProductCouponItemRepository;
import com.paca.paca.coupon.repository.ProductSubCategoryCouponItemRepository;
import com.paca.paca.coupon.statics.CouponStatics;
import com.paca.paca.coupon.utils.CouponMapper;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;
import com.paca.paca.sale.statics.TaxStatics;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final ProductCategoryCouponItemRepository productCategoryCouponItemRepository;
    private final ProductCouponItemRepository productCouponItemRepository;
    private final ProductSubCategoryCouponItemRepository productSubCategoryCouponItemRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSubCategoryRepository productSubCategoryRepository;
    private final BranchRepository branchRepository;
    private final CouponMapper couponMapper;
    private final Validator validator;

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

    public void delete(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Coupon with id " + id + " does not exists", 300));

        switch (coupon.getType()) {
            case CouponStatics.Type.PRODUCT:
                productCouponItemRepository.deleteAllByCoupon_Id(id);
                break;
            case CouponStatics.Type.CATEGORY:
                productCategoryCouponItemRepository.deleteAllByCoupon_Id(id);
                break;
            case CouponStatics.Type.SUB_CATEGORY:
                productSubCategoryCouponItemRepository.deleteAllByCoupon_Id(id);
                break;
            default:
                throw new NotFoundException(
                        "Coupon with id " + id + " does not have a valid type. Found: " + coupon.getType(), 301);
        }
        couponRepository.deleteById(id);
    }

    public CouponDTO save(Long branchId, CouponDTO dto) {
        Set<ConstraintViolation<CouponDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) throw new BadRequestException(violations.toString(), 302);

        if (dto.getEndDate().getTime() < dto.getStartDate().getTime())
            throw new BadRequestException("The date range is not valid", 302);

        if (!TaxStatics.Types.isTypeValid(dto.getDiscountType()))
            throw new BadRequestException("The discount type is not valid", 302);

        if (dto.getValue() < 0)
            throw new BadRequestException("The discount value must be positive", 302);

        if (couponRepository.existsById(dto.getId())) throw new
                BadRequestException("The given coupon already exists", 302);

        if (dto.getItems().isEmpty() || dto.getItems().size() > 5)
            throw new BadRequestException("The coupon must have at least one item and at most 5 items", 302);

        Coupon coupon = couponMapper.toEntity(dto);
        couponRepository.save(coupon);

        switch (coupon.getType()) {
            case CouponStatics.Type.PRODUCT:
                productCouponItemRepository.saveAll(dto.getItems().stream().map(item -> {
                    Product product = productRepository.findById(item.getId()).orElseThrow(
                            () -> new BadRequestException("Does not exists a product with id " + item.getId()));
                    return couponMapper.toProductItemEntity(item, product, coupon);
                }).collect(Collectors.toList()));
                break;
            case CouponStatics.Type.CATEGORY:
                productCategoryCouponItemRepository.saveAll(dto.getItems().stream().map(item -> {
                    ProductCategory productCategory = productCategoryRepository.findById(item.getId()).orElseThrow(
                            () -> new BadRequestException(
                                    "Does not exists a product category with id " + item.getId()));
                    Branch branch = branchRepository.findById(branchId).orElseThrow(
                            () -> new BadRequestException("Does not exists a branch with id " + item.getId()));
                    return couponMapper.toProductCategoryItemEntity(item, productCategory, coupon, branch);
                }).collect(Collectors.toList()));
                break;
            case CouponStatics.Type.SUB_CATEGORY:
                productSubCategoryCouponItemRepository.saveAll(dto.getItems().stream().map(item -> {
                    ProductSubCategory productSubCategory = productSubCategoryRepository.findById(item.getId())
                            .orElseThrow(() -> new BadRequestException(
                                    "Does not exists a product sub category with id " + item.getId()));
                    return couponMapper.toProductSubCategoryItemEntity(item, productSubCategory, coupon);
                }).collect(Collectors.toList()));
                break;
            default: throw new NotFoundException("The discount type is not valid", 302);
        }

        return CouponDTO.builder().build();
    }

    public CouponDTO update(CouponDTO dto) {

        return CouponDTO.builder().build();
    }

}
