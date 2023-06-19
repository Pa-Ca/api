package com.paca.paca.promotion.utils;

import org.mapstruct.*;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(source = "branch.id", target = "branchId")
    PromotionDTO toDTO(Promotion promotion);

    @Mapping(target = "branch", ignore = true)
    Promotion toEntity(PromotionDTO dto);

    default Promotion toEntity(PromotionDTO dto, Branch branch) {
        Promotion promotion = toEntity(dto);
        promotion.setBranch(branch);
        return promotion;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Promotion updateModel(PromotionDTO dto, @MappingTarget Promotion promotion);
}
