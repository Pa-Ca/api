package com.paca.paca.user.repository;

import com.paca.paca.statics.UserRole;
import com.paca.paca.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);

    Boolean existsByName(UserRole name);
}
