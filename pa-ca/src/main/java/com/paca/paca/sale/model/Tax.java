package com.paca.paca.sale.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_seq")
    @SequenceGenerator(name = "tax_seq", sequenceName = "tax_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private Short type;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Float value;
};
