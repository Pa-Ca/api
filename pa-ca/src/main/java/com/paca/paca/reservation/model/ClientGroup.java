package com.paca.paca.reservation.model;

import lombok.*;

import com.paca.paca.client.model.Client;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_group")
public class ClientGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_group_seq")
    @SequenceGenerator(name = "client_group_seq", sequenceName = "client_group_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "is_owner", nullable = false)
    private Boolean isOwner;
}
