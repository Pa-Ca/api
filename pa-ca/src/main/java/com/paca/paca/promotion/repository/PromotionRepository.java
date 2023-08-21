package com.paca.paca.promotion.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.promotion.model.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findAllByBranchId(Long id);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);
}
