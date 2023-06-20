package com.paca.paca.sale.utils;


import com.paca.paca.sale.model.Sale;
import com.paca.paca.branch.model.Table; //branch.model.Table;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.dto.SaleDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "reservation.id", target = "reservationId")
    SaleDTO toDTO(Sale sale);

    @Mapping(target = "table", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    Sale toEntity(SaleDTO dto);

    default Sale toEntity(SaleDTO dto, Table table, Reservation reservation) {
        Sale sale = toEntity(dto);
        sale.setTable(table);
        sale.setReservation(reservation);

        return sale;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "table", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sale updateModel(SaleDTO dto, @MappingTarget Sale sale);


}