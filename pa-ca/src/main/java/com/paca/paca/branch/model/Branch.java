package com.paca.paca.branch.model;

import com.paca.paca.business.model.Business;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "branch_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "address")
    private String address;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "name")
    private String name;

    @Column(name = "overview")
    private String overview;

    @Column(name = "score")
    private Float score;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "reservation_price")
    private Float reservationPrice;

    @Column(name = "reserve_off")
    private Boolean reserveOff;

    @Column(name = "average_reserve_time")
    private Float averageReserveTime;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "type")
    private String type;
}
