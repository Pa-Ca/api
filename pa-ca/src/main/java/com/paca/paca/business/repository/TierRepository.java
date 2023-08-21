package com.paca.paca.business.repository;

import com.paca.paca.business.model.Tier;
import com.paca.paca.statics.BusinessTier;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface TierRepository extends JpaRepository<Tier, Long> {

    Optional<Tier> findByName(BusinessTier name);
}
