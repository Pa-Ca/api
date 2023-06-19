package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.Tax;


import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {
    
    List<Tax> findAllBySaleId(Long saleId);  
}
