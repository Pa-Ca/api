package com.paca.paca.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.paca.paca.client.model.Review;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findById(Long id);

    Optional<Review> findByClientIdAndBranchId(Long clientId, Long branchId);

    List<Review> findAllByBranchId(Long id);

    Boolean existsByIdAndClientId(Long id, Long clientId);
}
