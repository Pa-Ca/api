package com.paca.paca.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.paca.paca.client.model.ReviewLike;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByClientIdAndReviewId(Long clientId, Long reviewId);

    Boolean existsByClientIdAndReviewId(Long clientId, Long reviewId);

    List<ReviewLike> findAllByReviewId(Long id);
}
