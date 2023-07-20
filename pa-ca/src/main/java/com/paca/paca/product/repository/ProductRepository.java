package com.paca.paca.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    List<Product> findAllBySubCategoryId(Long id);

    List<Product> findAllBySubCategory_Branch_Id(Long id);

    Boolean existsByIdAndSubCategory_Branch_Business_Id(Long id, Long businessId);

    Boolean existsBySubCategoryIdAndName(Long subCategoryId, String name);
}
