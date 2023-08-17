package com.paca.paca.branch.repository;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.branch.model.DefaultTax;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface DefaultTaxRepository extends JpaRepository<DefaultTax, Long> {

    @Query("SELECT t " +
            "FROM Tax t, DefaultTax d " +
            "WHERE (t.id = d.tax.id) AND (d.branch.id = :branchId)")
    List<Tax> findAllByBranchId(@Param("branchId") Long branchId);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);

}
