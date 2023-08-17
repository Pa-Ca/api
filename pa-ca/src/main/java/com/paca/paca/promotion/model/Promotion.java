package com.paca.paca.promotion.model;

import com.paca.paca.branch.model.Branch;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotion_seq")
    @SequenceGenerator(name = "promotion_seq", sequenceName = "promotion_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;
}
