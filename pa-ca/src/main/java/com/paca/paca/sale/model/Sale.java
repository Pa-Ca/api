package com.paca.paca.sale.model;

import java.math.BigDecimal;
import java.util.Date;

import com.paca.paca.reservation.model.Reservation;

import lombok.*;

import jakarta.persistence.*;


@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq", sequenceName = "sale_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "table_id")
    private Table table;

    @Column(name = "client_quantity")
    private Integer clientQuantity;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "table_name")
    private String table_name;

    @Column(name = "dollar_to_local_currency_exchange")
    private BigDecimal dollarToLocalCurrencyExchange;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
