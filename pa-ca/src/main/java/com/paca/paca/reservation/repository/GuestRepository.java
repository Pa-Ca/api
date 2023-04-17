package com.paca.paca.reservation.repository;

import com.paca.paca.reservation.model.Guest;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

}
