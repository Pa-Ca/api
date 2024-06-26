package com.paca.paca.reservation.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.reservation.model.ClientGroup;

@Repository
public interface ClientGroupRepository extends JpaRepository<ClientGroup, Long> {

    Optional<ClientGroup> findById(Long id);

    List<ClientGroup> findAllByClientId(Long id);

    List<ClientGroup> findAllByReservationId(Long id);

    List<ClientGroup> findAllByClientIdAndReservationReservationDateInGreaterThanEqual(
            Long id,
            Date reservationDateIn);

    Optional<ClientGroup> findByClientIdAndReservationId(Long clientId, Long reservationId);

    Boolean existsByReservationIdAndClientId(Long reservationId, Long clientId);

    Optional<ClientGroup> findByReservationIdAndClientId(Long reservationId, Long clientId);

}