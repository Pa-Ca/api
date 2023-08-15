package com.paca.paca.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import com.paca.paca.branch.model.Branch;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"favorite_branch\"")
public class FavoriteBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorite_branch_seq")
    @SequenceGenerator(name = "favorite_branch_seq", sequenceName = "favorite_branch_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
}
