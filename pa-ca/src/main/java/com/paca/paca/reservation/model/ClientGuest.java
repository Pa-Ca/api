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
@Table(name = "client_guest")
public class ClientGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_guest_seq")
    @SequenceGenerator(name = "client_guest_seq", sequenceName = "client_guest_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(name = "have_guest", nullable = false)
    private Boolean haveGuest;
}
