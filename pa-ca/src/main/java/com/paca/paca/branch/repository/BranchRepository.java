package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Branch;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByBusinessId(Long id);
}
