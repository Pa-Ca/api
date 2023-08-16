package com.paca.paca.reservation.model;

import java.util.Date;
import java.math.BigDecimal;

import com.paca.paca.branch.model.Branch;

import lombok.*;
import jakarta.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(name = "request_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "reservation_date_in", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDateIn;

    @Column(name = "reservation_date_out")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDateOut;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private Short status;

    @Column(name = "table_number", nullable = false)
    private Short tableNumber;

    @Column(name = "client_number", nullable = false)
    private Short clientNumber;

    @Column(name = "occasion", nullable = false)
    private String occasion;

    @Column(name = "by_client", nullable = false)
    private Boolean byClient;
}