package com.paca.paca.reservation.repository;

import com.paca.paca.reservation.model.Invoice;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
