package com.paca.paca.auth.repository;

import com.paca.paca.auth.model.JwtBlackList;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Long> {

    Optional<JwtBlackList> findByToken(String token);

    List<JwtBlackList> findAllByExpirationLessThan(Date date);

    void deleteAllByExpirationLessThan(Date date);
}
