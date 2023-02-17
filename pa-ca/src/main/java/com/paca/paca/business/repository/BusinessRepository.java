package com.paca.paca.business.repository;

import com.paca.paca.business.model.Business;
import com.paca.paca.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Client> findByBusinessId(Long userId);

    Boolean existsByBusinessId(Long userId);

    Boolean existsByUserEmail(String email);

}