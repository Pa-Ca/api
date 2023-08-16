package com.paca.paca.reservation.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.math.BigDecimal;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
    @SequenceGenerator(name = "invoice_seq", sequenceName = "invoice_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "pay_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "payment", nullable = false)
    private String payment;

    @Column(name = "payment_code", nullable = false)
    private String paymentCode;
}
