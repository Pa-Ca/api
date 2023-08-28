package com.paca.paca.sale.repository;

import com.paca.paca.sale.model.Sale;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s "
            + "FROM Sale s "
            + "WHERE s.branch.id = :branchId "
            + "    AND (COALESCE(:status) IS NULL OR (s.status IN :status)) "
            + "    AND (CAST(:startTime AS TIMESTAMP) IS NULL OR s.startTime >= :startTime) "
            + "    AND (CAST(:endTime AS TIMESTAMP) IS NULL OR s.startTime <= :endTime) "
            + "    AND ( "
            + "        (:name IS NULL "
            + "        AND :surname IS NULL "
            + "        AND :identityDocument IS NULL) "
            + "        OR EXISTS ( "
            + "            SELECT cg "
            + "            FROM ClientGuest cg, Client c, Guest g "
            + "            WHERE s.clientGuest.id = cg.id "
            + "                AND ( "
            + "                        (cg.guest.id = c.id "
            + "                        AND (:name IS NULL OR g.name ILIKE CONCAT('%', :name, '%')) "
            + "                        AND (:surname IS NULL OR g.surname ILIKE CONCAT('%', :surname, '%')) "
            + "                        AND (:identityDocument IS NULL OR g.identityDocument ILIKE CONCAT('%', :identityDocument, '%'))) "
            + "                    OR "
            + "                        (cg.client.id = c.id "
            + "                        AND (:name IS NULL OR c.name ILIKE CONCAT('%', :name, '%')) "
            + "                        AND (:surname IS NULL OR c.surname ILIKE CONCAT('%', :surname, '%')) "
            + "                        AND (:identityDocument IS NULL OR c.identityDocument ILIKE CONCAT('%', :identityDocument, '%')))))) "
            + "ORDER BY s.startTime DESC")
    List<Sale> findAllByBranchIdAndFilters(
            @Param("branchId") Long branchId,
            @Param("status") List<Short> status,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("identityDocument") String identityDocument);

    List<Sale> findAllByBranchIdAndStatusOrderByStartTimeDesc(Long branchId, Short status);

    Boolean existsByIdAndBranch_Business_Id(Long id, Long businessId);

    Boolean existsByBranchBusinessIdAndClientGuestGuestId(Long branchId, Long GuestId);

}
