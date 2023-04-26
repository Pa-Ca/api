package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Branch;

import io.micrometer.common.lang.Nullable;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Boolean existsByIdAndBusinessId(Long id, Long businessId);

    Optional<Branch> findByBusiness_UserEmail(String email);

    Page<Branch> findAllByReservationPriceBetweenAndScoreGreaterThanEqualAndCapacityGreaterThanEqual(
        @Nullable Float min_reservation_price, 
        @Nullable Float max_reservation_price, 
        @Nullable Float min_score,
        @Nullable Integer min_capacity, 
        @Nullable Pageable pageable);
    
}
