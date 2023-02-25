package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Review;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findById(Long id);

    Optional<Review> findByClientIdAndBranchId(Long clientId, Long branchId);

    List<Review> findAllByBranchId(Long id);
}
