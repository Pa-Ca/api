package com.paca.paca.business.repository;

import com.paca.paca.business.model.Business;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}
