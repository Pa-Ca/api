package com.paca.paca.sale.utils;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaxMapper {

    TaxDTO toDTO(Tax tax);

    Tax toEntity(TaxDTO dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tax updateModel(TaxDTO dto, @MappingTarget Tax tax);

}