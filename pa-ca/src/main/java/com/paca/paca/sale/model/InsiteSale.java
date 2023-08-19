package com.paca.paca.sale.model;

import lombok.*;

import com.paca.paca.reservation.model.Reservation;

import jakarta.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insite_sale")
public class InsiteSale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insite_sale_seq")
    @SequenceGenerator(name = "insite_sale_seq", sequenceName = "insite_sale_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
