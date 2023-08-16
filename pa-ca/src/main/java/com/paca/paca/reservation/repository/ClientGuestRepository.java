package com.paca.paca.reservation.repository;

import org.springframework.stereotype.Repository;
import com.paca.paca.reservation.model.ClientGuest;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClientGuestRepository extends JpaRepository<ClientGuest, Long> {

}
