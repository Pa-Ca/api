package com.paca.paca.branch.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "branch_amenity")
public class BranchAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_amenity_seq")
    @SequenceGenerator(name = "branch_amenity_seq", sequenceName = "branch_amenity_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;
}
