package com.paca.paca.promotion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PromotionDTO {

    private Long id;
    private Long BranchId;
    private Boolean disabled;
    private String text;
}
