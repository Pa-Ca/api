package com.paca.paca.business.repository;

import com.paca.paca.business.model.Business;
import com.paca.paca.client.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByUserId(Long userId);

    Boolean existsByUserId(Long userId);

    Optional<Business> findByUserEmail(String email);

    Boolean existsByUserEmail(String email);
}
