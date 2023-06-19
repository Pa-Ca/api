package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.BranchDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(source = "business.id", target = "businessId")
    BranchDTO toDTO(Branch branch);

    @Mapping(target = "business", ignore = true)
    Branch toEntity(BranchDTO dto);

    default Branch toEntity(BranchDTO dto, Business business) {
        Branch branch = toEntity(dto);
        branch.setBusiness(business);

        return branch;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Branch updateModel(BranchDTO dto, @MappingTarget Branch branch);
}