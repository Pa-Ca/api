package com.paca.paca.reservation.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.reservation.model.ClientGroup;

@Repository
public interface ClientGroupRepository extends JpaRepository<ClientGroup, Long> {

    Optional<ClientGroup> findById(Long id);

    List<ClientGroup> findAllByClientId(Long id);

    List<ClientGroup> findAllByReservationId(Long id);

    List<ClientGroup> findAllByClientIdAndReservationReservationDateGreaterThanEqual(
            Long id,
            Date reservationDate);

    Optional<ClientGroup> findByClientIdAndReservationId(Long clientId, Long reservationId);

}