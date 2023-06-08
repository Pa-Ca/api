package com.paca.paca.sale.dto;

import java.util.Date;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private Integer clientQuantity;
    private Date startTime;
    private Date endTime;
    private Integer status;
    private Long branchId;
    private String name;
}