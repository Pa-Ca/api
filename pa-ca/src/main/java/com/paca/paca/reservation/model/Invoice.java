package com.paca.paca.reservation.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.Date;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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
