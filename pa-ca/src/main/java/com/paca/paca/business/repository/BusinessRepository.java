package com.paca.paca.business.repository;

import java.util.List;
import java.util.Optional;

import com.paca.paca.branch.model.Amenity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.business.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findById(Long branchId);

    @Query(value="SELECT B.id FROM business B JOIN \"user\" U on U.id = B.user_id WHERE U.email = ?1", nativeQuery=true)
    Optional<Business> findBusinessByUserEmail(String userEmail);
}
