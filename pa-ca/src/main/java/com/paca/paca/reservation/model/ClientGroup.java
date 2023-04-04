package com.paca.paca.reservation.model;

import lombok.*;

import com.paca.paca.client.model.Client;

import jakarta.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_group")
public class ClientGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_group_seq")
    @SequenceGenerator(name = "client_group_seq", sequenceName = "client_group_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "is_owner")
    private Boolean isOwner;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "name")
    private String name;
}
