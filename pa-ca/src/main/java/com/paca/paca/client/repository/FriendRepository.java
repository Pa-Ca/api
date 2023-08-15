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

    // Accepted requests
    List<Friend> findAllByRequesterIdAndAcceptedTrue(Long userId);

    List<Friend> findAllByAddresserIdAndAcceptedTrue(Long userId);

    // Rejected requests
    List<Friend> findAllByAddresserIdAndRejectedTrue(Long userId);

    // Pending requests
    List<Friend> findAllByAddresserIdAndAcceptedFalseAndRejectedFalse(Long userId);

}
