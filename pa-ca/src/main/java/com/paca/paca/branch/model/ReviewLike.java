package com.paca.paca.branch.model;

import com.paca.paca.client.model.Client;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_like_seq")
    @SequenceGenerator(name = "review_like_seq", sequenceName = "review_like_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
