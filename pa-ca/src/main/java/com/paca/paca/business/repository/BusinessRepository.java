package com.paca.paca.business.repository;

import com.paca.paca.business.model.Business;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByUserId(Long userId);

    Optional<Business> findByUserEmail(String email);

    Boolean existsByUserEmail(String email);
}