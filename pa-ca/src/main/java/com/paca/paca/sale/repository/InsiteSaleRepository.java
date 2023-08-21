package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.InsiteSale;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InsiteSaleRepository extends JpaRepository<InsiteSale, Long> {

    Optional<InsiteSale> findBySaleId(Long saleId);
}