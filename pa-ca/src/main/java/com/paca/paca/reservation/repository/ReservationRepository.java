package com.paca.paca.reservation.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paca.paca.reservation.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByBranchId(Long id);

    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.branch.id = :branchId " +
            "    AND (COALESCE(:status) IS NULL OR (r.status IN :status)) " +
            "    AND (CAST(:startTime AS TIMESTAMP) IS NULL OR r.reservationDateIn >= :startTime) " +
            "    AND (CAST(:endTime AS TIMESTAMP) IS NULL OR r.reservationDateIn <= :endTime) " +
            "    AND ( " +
            "        ( " +
            "           :name IS NULL " +
            "           AND :surname IS NULL " +
            "           AND :identityDocument IS NULL) "
            +
            "        OR EXISTS ( " +
            "            SELECT g " +
            "            FROM Guest g " +
            "            WHERE g.id = r.guest.id " +
            "            AND (:name IS NULL OR LOWER(g.name) LIKE %:name%) " +
            "            AND (:surname IS NULL OR LOWER(g.surname) LIKE %:surname%) " +
            "            AND (:identityDocument IS NULL OR LOWER(g.identityDocument) LIKE %:identityDocument%)) "
            +
            "        OR EXISTS ( " +
            "            SELECT cg " +
            "            FROM ClientGroup cg " +
            "            WHERE cg.reservation.id = r.id " +
            "                AND cg.isOwner = TRUE " +
            "                AND (:name IS NULL OR LOWER(cg.client.name) LIKE %:name%) " +
            "                AND (:surname IS NULL OR LOWER(cg.client.surname) LIKE %:surname%) " +
            "                AND (:identityDocument IS NULL OR LOWER(cg.client.identityDocument) LIKE %:identityDocument%)))"
            +
            "ORDER BY r.reservationDateIn DESC")
    List<Reservation> findAllByBranchIdAndFilters(
            @Param("branchId") Long branchId,
            @Param("status") List<Integer> status,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("identityDocument") String identityDocument);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);

    Boolean existsByIdAndBranchId(Long id, Long businessId);

    Boolean existsByBranchBusinessIdAndGuestId(Long businessId, Long guestId);
}