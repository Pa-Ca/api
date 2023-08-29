package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.Tax;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {

    @Query("SELECT t " +
            "FROM Tax t, DefaultTax d " +
            "WHERE (t.id = d.tax.id) AND (d.branch.id = :branchId)")
    List<Tax> findAllByBranchId(@Param("branchId") Long branchId);
}
