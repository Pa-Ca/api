package com.paca.paca.reservation.utils;

import org.mapstruct.*;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.dto.InvoiceDTO;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceDTO toDTO(Invoice invoice);

    Invoice toEntity(InvoiceDTO dto);
}
