package com.paca.paca.branch.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "branch_amenity")
public class BranchAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_amenity_seq")
    @SequenceGenerator(name = "branch_amenity_seq", sequenceName = "branch_amenity_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;
}
