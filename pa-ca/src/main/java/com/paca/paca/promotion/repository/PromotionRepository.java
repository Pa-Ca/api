package com.paca.paca.promotion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.promotion.model.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Optional<Promotion> findById(Long id);

    List<Promotion> findAllByBranchId(Long id);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);
}
