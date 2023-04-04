package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Branch;

import com.paca.paca.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Boolean existsByIdAndBusinessId(Long id, Long businessId);

    Optional<Branch> findByBusiness_UserEmail(String email);
}
