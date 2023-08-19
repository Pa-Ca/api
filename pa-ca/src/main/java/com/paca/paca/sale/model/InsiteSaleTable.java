package com.paca.paca.sale.model;

import lombok.*;

import jakarta.persistence.*;

import com.paca.paca.branch.model.Table;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "insite_sale_table")
public class InsiteSaleTable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insite_sale_seq")
    @SequenceGenerator(name = "insite_sale_seq", sequenceName = "insite_sale_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "insite_sale_id", nullable = false)
    private InsiteSale insiteSale;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;
}
