package com.paca.paca.client.model;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"friend\"")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_seq")
    @SequenceGenerator(name = "friend_seq", sequenceName = "friend_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_requester_id", nullable = false)
    private Client requester;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_addresser_id", nullable = false)
    private Client addresser;

    @Column(name = "accepted", nullable = false)
    private Boolean accepted;

    @Column(name = "rejected", nullable = false)
    private Boolean rejected;
}
