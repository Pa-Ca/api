package com.paca.paca.reservation.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.reservation.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByBranchId(Long id);

    List<Reservation> findAllByBranchIdAndReservationDateInGreaterThanEqual(
            Long id,
            Date reservationDateIn);
    
    Page<Reservation> findAllByBranchIdAndReservationDateInGreaterThanEqual(
        Long id,
        Date reservationDateIn,
        Pageable pageable);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);

    Boolean existsByIdAndBranchId(Long id, Long businessId);
}