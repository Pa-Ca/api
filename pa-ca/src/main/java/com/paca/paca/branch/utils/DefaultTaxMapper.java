package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.dto.DefaultTaxDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DefaultTaxMapper {

    @Mapping(source = "branch.id", target = "branchId")
    DefaultTaxDTO toDTO(DefaultTax defaultTax);

    @Mapping(target = "branch", ignore = true)
    DefaultTax toEntity(DefaultTaxDTO dto);

    default DefaultTax toEntity(DefaultTaxDTO dto, Branch branch) {
        DefaultTax defaultTax = toEntity(dto);
        defaultTax.setBranch(branch);
        return defaultTax;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DefaultTax updateModel(DefaultTaxDTO dto, @MappingTarget DefaultTax defaultTax);


}