package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.OnlineSale;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OnlineSaleRepository extends JpaRepository<OnlineSale, Long> {
}