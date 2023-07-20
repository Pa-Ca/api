package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.DefaultTax;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface DefaultTaxRepository extends JpaRepository<DefaultTax, Long> {
    
    List<DefaultTax> findAllByBranchId(Long branchId);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId); // Needs to be tested

}
