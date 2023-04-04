package com.paca.paca.reservation.model;

import java.util.Date;

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
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "request_date")
    @Temporal(TemporalType.DATE)
    private Date requestDate;

    @Column(name = "reservation_date")
    @Temporal(TemporalType.DATE)
    private Date reservationDate;

    @Column(name = "client_number")
    private Integer clientNumber;

    @Column(name = "payment")
    private String payment;

    @Column(name = "status")
    private Integer status;

    @Column(name = "pay_date")
    @Temporal(TemporalType.DATE)
    private Date payDate;

    @Column(name = "price")
    private Float price;

    @Column(name = "occasion")
    private String occasion;

    @Column(name = "petition")
    private String petition;
    @Column(name = "by_client")
    private Boolean byClient;
}
