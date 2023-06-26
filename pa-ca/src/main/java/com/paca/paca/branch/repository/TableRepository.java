package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Table;


import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

    List<Table> findAllByBranchIdAndDeletedFalse(long branchId);
}
