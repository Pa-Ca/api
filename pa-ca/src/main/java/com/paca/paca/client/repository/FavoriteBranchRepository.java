package com.paca.paca.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.client.model.FavoriteBranch;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteBranchRepository extends JpaRepository<FavoriteBranch, Long> {

    Boolean existsByClientIdAndBranchId(Long clientId, Long branchId);

    Optional<FavoriteBranch> findByClientIdAndBranchId(Long clientId, Long branchId);

    List<FavoriteBranch> findAllByClientId(Long clientId);
}
