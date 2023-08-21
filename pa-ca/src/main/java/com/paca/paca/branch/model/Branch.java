package com.paca.paca.branch.model;

import java.time.Duration;
import java.time.LocalTime;
import java.math.BigDecimal;

import org.hibernate.annotations.Type;

import com.paca.paca.business.model.Business;

import jakarta.persistence.*;

import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "branch_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private Float score;

    @Column(name = "capacity", nullable = false)
    private Short capacity;

    @Column(name = "reservation_price", nullable = false)
    private BigDecimal reservationPrice;

    @Column(name = "maps_link")
    private String mapsLink;

    @Column(name = "location")
    private String location;

    @Column(name = "overview")
    private String overview;

    @Column(name = "visibility", nullable = false)
    private Boolean visibility;

    @Column(name = "reserve_off")
    private Boolean reserveOff;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "hour_in")
    private LocalTime hourIn;

    @Column(name = "hour_out")
    private LocalTime hourOut;

    @Type(PostgreSQLIntervalType.class)
    @Column(name = "average_reserve_time")
    private Duration averageReserveTime;

    @Column(name = "dollar_exchange")
    private Float dollarExchange;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
