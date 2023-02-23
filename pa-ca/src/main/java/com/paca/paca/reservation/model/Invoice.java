package com.paca.paca.reservation.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @Column(name = "id")
    private long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
