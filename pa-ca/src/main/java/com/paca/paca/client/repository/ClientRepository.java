package com.paca.paca.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paca.paca.client.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserId(Long userId);

    Boolean existsByUserId(Long userId);

    Optional<Client> findByUserEmail(String email);

    Boolean existsByUserEmail(String email);
}