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

    Page<Sale> findAllByBranchIdAndStatusAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            Long branchId,
            @Nullable Integer status,
            @Nullable Date startTime,
            @Nullable Date endTime,
            Pageable pageable);
    
    Page<Sale> findAllByBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
        Long branchId,
        Collection<Integer> status,
        @Nullable Date startTime,
        @Nullable Date endTime,
        Pageable pageable);

    List<Sale> findAllByBranchIdAndStatusOrderByStartTimeDesc(Long branchId, Integer status);

}

