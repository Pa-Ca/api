package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.BranchAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchAmenityRepository extends JpaRepository<BranchAmenity, Long> {

    List<BranchAmenity> findAllByBranchId(Long id);

    List<BranchAmenity> findAllByAmenityId(Long id);

    boolean existsByBranchIdAndAmenityId(Long branchId, Long amenityId);

    Optional<BranchAmenity> findByBranchIdAndAmenityId(Long branchId, Long amenityId);
}
