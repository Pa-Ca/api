package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.dto.TableDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TableMapper {

    @Mapping(source = "branch.id", target = "branchId")
    TableDTO toDTO(Table Table);

    @Mapping(target = "branch", ignore = true)
    Table toEntity(TableDTO dto);

    default Table toEntity(TableDTO dto, Branch branch) {
        Table table = toEntity(dto);
        table.setBranch(branch);
        return table;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Table updateModel(TableDTO dto, @MappingTarget Table table);


}