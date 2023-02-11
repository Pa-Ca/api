package com.paca.paca.promotion.utils;

import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionDTO toDTO(Promotion promotion);

    Promotion toEntity(PromotionDTO dto);

    default Promotion updateModel(Promotion promotion, PromotionDTO dto) {
        if (dto.getDisabled() != null) {
            promotion.setDisabled(dto.getDisabled());
        }
        if (dto.getText() != null) {
            promotion.setText(dto.getText());
        }

        return promotion;
    }
}
