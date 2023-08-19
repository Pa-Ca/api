package com.paca.paca.promotion.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PromotionListDTO {
    List<PromotionDTO> promotions;
}
