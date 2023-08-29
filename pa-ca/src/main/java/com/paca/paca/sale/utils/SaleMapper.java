package com.paca.paca.sale.utils;

import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.model.Invoice;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "clientGuest.id", target = "clientGuestId")
    SaleDTO toDTO(Sale sale);

    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "clientGuest", ignore = true)
    Sale toEntity(SaleDTO dto);

    default Sale toEntity(SaleDTO dto, Branch branch, Invoice invoice, ClientGuest clientGuest) {
        Sale sale = toEntity(dto);
        sale.setBranch(branch);
        sale.setInvoice(invoice);
        sale.setClientGuest(clientGuest);

        return sale;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "clientGuest", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sale updateModel(SaleDTO dto, @MappingTarget Sale sale);

}