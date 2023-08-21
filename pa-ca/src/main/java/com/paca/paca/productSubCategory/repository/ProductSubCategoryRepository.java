package com.paca.paca.productSubCategory.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.productSubCategory.model.ProductSubCategory;

@Repository
public interface ProductSubCategoryRepository extends JpaRepository<ProductSubCategory, Long> {

    List<ProductSubCategory> findAllByBranchId(Long branchId);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);

    Boolean existsByBranchIdAndCategoryIdAndName(Long branchId, Long categoryId, String name);
}
