package com.paca.paca.promotion.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {

    private Long id;
    private Long branchId;
    private String text;
    private Boolean disabled;
}
