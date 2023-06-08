package com.paca.paca.sale.utils;


import com.paca.paca.sale.model.Sale;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.dto.SaleDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "branch.id", target = "branchId")
    SaleDTO toDTO(Sale sale);

    @Mapping(target = "branch", ignore = true)
    Sale toEntity(SaleDTO dto);

    default Sale toEntity(SaleDTO dto, Branch branch) {
        Sale sale = toEntity(dto);
        sale.setBranch(branch);

        return sale;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sale updateModel(SaleDTO dto, @MappingTarget Sale sale);


}