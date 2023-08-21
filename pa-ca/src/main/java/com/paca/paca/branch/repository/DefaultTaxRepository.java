package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.DefaultTax;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DefaultTaxRepository extends JpaRepository<DefaultTax, Long> {

    Boolean existsByTaxIdAndBranch_Business_Id(Long id, Long businessId);

}
