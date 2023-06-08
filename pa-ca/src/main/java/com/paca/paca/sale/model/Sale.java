package com.paca.paca.sale.model;

import java.util.Date;

import lombok.*;

import jakarta.persistence.*;

import com.paca.paca.branch.model.Branch;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq", sequenceName = "sale_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "client_quantity")
    private Integer clientQuantity;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "name")
    private String name;
}
