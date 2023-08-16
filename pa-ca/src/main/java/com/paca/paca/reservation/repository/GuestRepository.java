package com.paca.paca.reservation.repository;

import java.util.Optional;

import com.paca.paca.reservation.model.Guest;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByIdentityDocument(String identityDocument);
}