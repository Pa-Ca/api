package com.paca.paca.sale.utils;


import com.paca.paca.sale.model.Sale;
import com.paca.paca.branch.model.Table; //branch.model.Table;
import com.paca.paca.sale.dto.SaleDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "table.id", target = "tableId")
    SaleDTO toDTO(Sale sale);

    @Mapping(target = "table", ignore = true)
    Sale toEntity(SaleDTO dto);

    default Sale toEntity(SaleDTO dto, Table table) {
        Sale sale = toEntity(dto);
        sale.setTable(table);

        return sale;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "table", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sale updateModel(SaleDTO dto, @MappingTarget Sale sale);


}