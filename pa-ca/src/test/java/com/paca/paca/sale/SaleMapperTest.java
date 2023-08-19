package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.utils.SaleMapperImpl;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Date;

@ExtendWith(SpringExtension.class)
public class SaleMapperTest {

    @InjectMocks
    private SaleMapperImpl saleMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapSaleEntityToSaleDTO() {
        Sale sale = utils.createSale(null, null, null);

        SaleDTO response = saleMapper.toDTO(sale);
        SaleDTO expected = new SaleDTO(
                sale.getId(),
                sale.getBranch().getId(),
                sale.getClientGuest().getId(),
                sale.getInvoice().getId(),
                sale.getClientQuantity(),
                sale.getStatus(),
                sale.getStartTime(),
                sale.getEndTime(),
                sale.getDollarExchange(),
                sale.getNote());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapSaleDTOtoSaleEntity() {
        Sale sale = utils.createSale(null, null, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);

        Sale response = saleMapper.toEntity(saleDTO, sale.getBranch(), sale.getInvoice(), sale.getClientGuest());
        Sale expected = new Sale(
                sale.getId(),
                sale.getBranch(),
                sale.getClientGuest(),
                sale.getInvoice(),
                sale.getClientQuantity(),
                sale.getStatus(),
                sale.getStartTime(),
                sale.getEndTime(),
                sale.getDollarExchange(),
                sale.getNote());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapSaleDTOtoSaleEntity() {
        Sale sale = utils.createSale(null, null, null);

        SaleDTO saleDTO = new SaleDTO(
                sale.getId() + 1,
                sale.getBranch().getId() + 1,
                sale.getClientGuest().getId() + 1,
                sale.getInvoice().getId() + 1,
                (short) (sale.getClientQuantity() + 1),
                (short) (sale.getStatus() + 1),
                new Date(),
                new Date(),
                sale.getDollarExchange() + 1,
                sale.getNote() + ".");
        Sale response = saleMapper.updateModel(saleDTO, sale);
        Sale expected = new Sale(
                sale.getId(),
                sale.getBranch(),
                sale.getClientGuest(),
                sale.getInvoice(),
                saleDTO.getClientQuantity(),
                saleDTO.getStatus(),
                sale.getStartTime(),
                saleDTO.getEndTime(),
                saleDTO.getDollarExchange(),
                saleDTO.getNote());

        assertThat(response).isEqualTo(expected);
    }
}
