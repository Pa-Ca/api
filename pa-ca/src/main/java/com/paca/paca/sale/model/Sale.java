package com.paca.paca.sale.model;

import java.util.Date;

import lombok.*;

import jakarta.persistence.*;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.model.Invoice;

@Builder
@jakarta.persistence.Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq", sequenceName = "sale_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_guest_id", nullable = false)
    private ClientGuest clientGuest;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(name = "client_quantity", nullable = false)
    private Short clientQuantity;

    @Column(name = "status", nullable = false)
    private Short status;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "dollar_exchange", nullable = false)
    private Float dollarExchange;

    @Column(name = "note", nullable = false)
    private String note;
}
