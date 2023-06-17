package com.paca.paca.sale.utils;

import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaxMapper {

    @Mapping(source = "sale.id", target = "saleId")
    TaxDTO toDTO(Tax tax);

    @Mapping(target = "sale", ignore = true)
    Tax toEntity(TaxDTO dto);

    default Sale toEntity(TaxDTO dto, Sale sale) {
        Tax saleProduct = toEntity(dto);
        saleProduct.setSale(sale);
        return sale;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tax updateModel(TaxDTO dto, @MappingTarget Tax tax);


}