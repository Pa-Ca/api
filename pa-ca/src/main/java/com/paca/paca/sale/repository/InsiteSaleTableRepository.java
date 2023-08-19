package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.InsiteSaleTable;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InsiteSaleTableRepository extends JpaRepository<InsiteSaleTable, Long> {

    List<InsiteSaleTable> findAllByInsiteSaleId(Long id);
}