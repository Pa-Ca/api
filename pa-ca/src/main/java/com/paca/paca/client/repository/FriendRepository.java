package com.paca.paca.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.client.model.Friend;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByRequesterIdAndAddresserId(Long requesterId, Long addresserId);

    Boolean existsByRequesterIdAndAddresserId(Long requesterId, Long addresserId);

    // Solicitudes aceptadas
    List<Friend> findByRequesterIdAndAcceptedTrue(Long userId);

    List<Friend> findByAddresserIdAndAcceptedTrue(Long userId);

    // Solicitudes rechazadas
    List<Friend> findByRequesterIdAndRejectedTrue(Long userId);

    List<Friend> findByAddresserIdAndRejectedTrue(Long userId);

    // Solicitudes pendientes
    List<Friend> findByRequesterIdAndAcceptedFalseAndRejectedFalse(Long userId);

    List<Friend> findByAddresserIdAndAcceptedFalseAndRejectedFalse(Long userId);

}
