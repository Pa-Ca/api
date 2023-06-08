package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.Sale;

import org.springframework.stereotype.Repository;


// import date
import java.util.Date;

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


}