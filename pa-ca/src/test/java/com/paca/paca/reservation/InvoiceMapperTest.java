package com.paca.paca.reservation;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.dto.InvoiceDTO;
import com.paca.paca.reservation.utils.InvoiceMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class InvoiceMapperTest {

    @InjectMocks
    private InvoiceMapperImpl invoiceMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapInvoiceEntityToInvoiceDTO() {
        Invoice invoice = utils.createInvoice();

        InvoiceDTO response = invoiceMapper.toDTO(invoice);
        InvoiceDTO expected = new InvoiceDTO(
                invoice.getId(),
                invoice.getPayDate(),
                invoice.getPrice(),
                invoice.getPayment(),
                invoice.getPaymentCode());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapInvoiceDTOtoInvoiceEntity() {
        Invoice request = utils.createInvoice();
        InvoiceDTO dto = utils.createInvoiceDTO(request);

        Invoice requestMapped = invoiceMapper.toEntity(dto);
        Invoice expected = new Invoice(
                dto.getId(),
                dto.getPayDate(),
                dto.getPrice(),
                dto.getPayment(),
                dto.getPaymentCode());

        assertThat(requestMapped).isEqualTo(expected);
    }

}
