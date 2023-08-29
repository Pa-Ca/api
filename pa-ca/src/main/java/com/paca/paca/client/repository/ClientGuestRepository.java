package com.paca.paca.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.client.model.ClientGuest;

import java.util.Optional;

@Repository
public interface ClientGuestRepository extends JpaRepository<ClientGuest, Long> {

    Optional<ClientGuest> findByClientId(Long clientId);

    Optional<ClientGuest> findByGuestId(Long guestId);
}