package com.paca.paca.sale.repository;

import java.util.List;

import com.paca.paca.sale.model.SaleTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTaxRepository extends JpaRepository<SaleTax, Long> {

    List<SaleTax> findAllBySaleId(long saleId);

    void deleteAllBySaleId(long saleId);

    Boolean existsByTaxIdAndSale_Branch_Business_Id(Long id, Long businessId);
}