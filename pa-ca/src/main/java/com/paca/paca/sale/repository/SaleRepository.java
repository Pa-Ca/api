package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.Sale;

import org.springframework.stereotype.Repository;

import java.util.Collection;
// import date
import java.util.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.micrometer.common.lang.Nullable;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    Page<Sale> findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
        Long branchId,
        Collection<Integer> status,
        @Nullable Date startTime,
        Pageable pageable);

    List<Sale> findAllByTableBranchIdAndStatusOrderByStartTimeDesc(Long branchId, Integer status);

    Boolean existsByIdAndTable_Branch_Business_Id(Long id, Long businessId); // Needs to be tested

}

