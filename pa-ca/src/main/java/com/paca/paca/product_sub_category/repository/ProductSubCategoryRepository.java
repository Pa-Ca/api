package com.paca.paca.product_sub_category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.product_sub_category.model.ProductSubCategory;

@Repository
public interface ProductSubCategoryRepository extends JpaRepository<ProductSubCategory, Long> {
    Optional<ProductSubCategory> findById(Long productSubCategoryId);

    List<ProductSubCategory> findAllByBranchIdAndCategoryId(
            Long branchId,
            Long categoryId);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);
}
