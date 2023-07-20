package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.Sale;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT s " +
            "FROM Sale s " +
            "WHERE s.table.branch.id = :branchId " +
            "    AND (COALESCE(:status) IS NULL OR (s.status IN :status)) " +
            "    AND (CAST(:startTime AS TIMESTAMP) IS NULL OR s.startTime >= :startTime) " +
            "    AND (CAST(:endTime AS TIMESTAMP) IS NULL OR s.startTime <= :endTime) " +
            "    AND ( " +
            "        ( " +
            "           :name IS NULL " +
            "           AND :surname IS NULL " +
            "           AND :identityDocument IS NULL) "
            +
            "        OR EXISTS ( " +
            "            SELECT r " +
            "            FROM Reservation r, Guest g " +
            "            WHERE r.id = s.reservation.id " +
            "            AND g.id = s.reservation.guest.id " +
            "            AND (:name IS NULL OR LOWER(s.reservation.guest.name) LIKE %:name%) "
            +
            "            AND (:surname IS NULL OR LOWER(s.reservation.guest.surname) LIKE %:surname%) " +
            "            AND (:identityDocument IS NULL OR LOWER(s.reservation.guest.identityDocument) LIKE %:identityDocument%)) "
            +
            "        OR EXISTS ( " +
            "            SELECT g " +
            "            FROM ClientGroup g " +
            "            WHERE g.reservation.id = s.reservation.id " +
            "                AND g.isOwner = TRUE " +
            "                AND (:name IS NULL OR LOWER(g.client.name) LIKE %:name%) " +
            "                AND (:surname IS NULL OR LOWER(g.client.surname) LIKE %:surname%) " +
            "                AND (:identityDocument IS NULL OR LOWER(g.client.identityDocument) LIKE %:identityDocument%)))")
    Page<Sale> findAllByTableBranchIdAndFilters(
            @Param("branchId") Long branchId,
            @Param("status") List<Integer> status,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("identityDocument") String identityDocument,
            Pageable pageable);

    List<Sale> findAllByTableBranchIdAndStatusOrderByStartTimeDesc(Long branchId, Integer status);

    Boolean existsByIdAndTable_Branch_Business_Id(Long id, Long businessId); // Needs to be tested

}
