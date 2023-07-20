package com.paca.paca.branch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultTaxDTO {
    private Long id;
    private String name;
    private Integer type;
    private Float value;
    private Long branchId;  
}
