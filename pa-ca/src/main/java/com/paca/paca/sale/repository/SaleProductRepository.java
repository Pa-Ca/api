package com.paca.paca.sale.repository;

import java.util.List;

import com.paca.paca.sale.model.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    List<SaleProduct> findAllBySaleId(long saleId);

    void deleteAllBySaleId(long saleId);

    Boolean existsByIdAndSale_Branch_Business_Id(Long id, Long businessId);
}