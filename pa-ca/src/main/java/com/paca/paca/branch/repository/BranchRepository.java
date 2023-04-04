package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Branch;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Boolean existsByIdAndBusinessId(Long id, Long businessId);

    Optional<Branch> findByBusiness_UserEmail(String email);
}
