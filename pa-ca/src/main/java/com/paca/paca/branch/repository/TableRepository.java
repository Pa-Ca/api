package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Table;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

    List<Table> findAllByBranchIdAndDeletedFalse(long branchId);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId); // Needs to be tested
}
