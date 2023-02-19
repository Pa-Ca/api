package com.paca.paca.promotion.utils;

import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(source = "branch.id", target = "branchId")
    PromotionDTO toDTO(Promotion promotion);

    Promotion toEntity(PromotionDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Promotion updateModel(PromotionDTO dto, @MappingTarget Promotion promotion);
}
