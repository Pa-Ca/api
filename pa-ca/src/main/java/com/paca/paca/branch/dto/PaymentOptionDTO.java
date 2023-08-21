package com.paca.paca.branch.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOptionDTO {

    private Long id;
    private Long branchId;
    private String name;
    private String description;
}
