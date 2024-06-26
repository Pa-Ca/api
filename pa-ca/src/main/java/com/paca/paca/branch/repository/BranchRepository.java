package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Branch;

import io.micrometer.common.lang.Nullable;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Boolean existsByIdAndBusinessId(Long id, Long businessId);

    Optional<Branch> findByBusiness_UserEmail(String email);

    List<Branch> findAllByBusinessId(Long businessId);

    Page<Branch> findAllByReservationPriceBetweenAndScoreGreaterThanEqualAndCapacityGreaterThanEqual(
            @Nullable BigDecimal min_reservation_price,
            @Nullable BigDecimal max_reservation_price,
            @Nullable Float min_score,
            @Nullable Integer min_capacity,
            @Nullable Pageable pageable);

}
