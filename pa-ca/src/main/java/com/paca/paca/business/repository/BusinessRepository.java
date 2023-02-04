package com.paca.paca.business.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.business.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findAllById(Long id);

    Optional<Business> findById(Long branchId, Long amenityId);
}
