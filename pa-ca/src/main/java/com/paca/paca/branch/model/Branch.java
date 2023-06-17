package com.paca.paca.branch.model;

import java.time.Duration;
import java.time.LocalTime;
import java.math.BigDecimal;

import org.hibernate.annotations.Type;

import com.paca.paca.business.model.Business;

import jakarta.persistence.*;

import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;

import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "branch_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "location")
    private String location;

    @Column(name = "maps_link")
    private String mapsLink;

    @Column(name = "name")
    private String name;

    @Column(name = "overview")
    private String overview;

    @Column(name = "score")
    private Float score;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "reservation_price")
    private BigDecimal reservationPrice;

    @Column(name = "reserve_off")
    private Boolean reserveOff;

    @Type(PostgreSQLIntervalType.class)
    @Column(name = "average_reserve_time")
    private Duration averageReserveTime;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "hour_in")
    private LocalTime hourIn;

    @Column(name = "hour_out")
    private LocalTime hourOut;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "dollar_to_local_currency_exchange")
    private BigDecimal dollarToLocalCurrencyExchange;
}
