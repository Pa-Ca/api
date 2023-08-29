package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.PaymentOption;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PaymentOptionRepository extends JpaRepository<PaymentOption, Long> {

    List<PaymentOption> findAllByBranchId(Long branchId);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);
}
