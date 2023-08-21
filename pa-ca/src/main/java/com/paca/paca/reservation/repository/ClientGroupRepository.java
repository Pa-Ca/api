package com.paca.paca.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.reservation.model.ClientGroup;

@Repository
public interface ClientGroupRepository extends JpaRepository<ClientGroup, Long> {

    List<ClientGroup> findAllByClientId(Long id);

    List<ClientGroup> findAllByReservationId(Long id);

    Optional<ClientGroup> findByReservationIdAndClientId(Long reservationId, Long clientId);

}