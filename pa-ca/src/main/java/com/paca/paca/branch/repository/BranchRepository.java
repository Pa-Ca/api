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
        @Nullable Float lowerLimit, 
        @Nullable Float upperLimit, 
        @Nullable Float score,
        @Nullable Integer capacity, 
        @Nullable Pageable pageable);



}
