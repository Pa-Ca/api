package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.model.Table;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.utils.SaleMapperImpl;


import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.util.Date;


@ExtendWith(SpringExtension.class)
public class SaleMapperTest {
    
    @InjectMocks
    private SaleMapperImpl saleMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapSaleEntityToSaleDTO(){

        Sale sale = utils.createSale(null, null, null);

        SaleDTO saleDTO = saleMapper.toDTO(sale);

        // Check if the saleDTO is not null
        assertThat(saleDTO).isNotNull();
        // Check all the attributes of the saleDTO
        assertThat(saleDTO.getId()).isEqualTo(sale.getId());
        assertThat(saleDTO.getClientQuantity()).isEqualTo(sale.getClientQuantity());
        assertThat(saleDTO.getStartTime()).isEqualTo(sale.getStartTime());
        assertThat(saleDTO.getEndTime()).isEqualTo(sale.getEndTime());
        assertThat(saleDTO.getStatus()).isEqualTo(sale.getStatus());
        assertThat(saleDTO.getTableId()).isEqualTo(sale.getTable().getId());
        assertThat(saleDTO.getTableName()).isEqualTo(sale.getTable().getName());
        assertThat(saleDTO.getNote()).isEqualTo(sale.getNote());
        assertThat(saleDTO.getDollarToLocalCurrencyExchange()).isEqualTo(sale.getDollarToLocalCurrencyExchange());
        assertThat(saleDTO.getReservationId()).isEqualTo(sale.getReservation().getId());
    }

    @Test
    void shouldMapSaleDTOtoSaleEntity(){
            
            // Create a Table and a Reservation to be used in the Sale
            Table table = utils.createTable(null);
            Reservation reservation = utils.createReservation(null, null);

            SaleDTO saleDTO = utils.createSaleDTO(table, reservation);
    
            Sale sale = saleMapper.toEntity(saleDTO, table, reservation, null);
    
            // Check if the sale is not null
            assertThat(sale).isNotNull();
            // Check all the attributes of the sale
            assertThat(sale.getId()).isEqualTo(saleDTO.getId());
            assertThat(sale.getClientQuantity()).isEqualTo(saleDTO.getClientQuantity());
            assertThat(sale.getStartTime()).isEqualTo(saleDTO.getStartTime());
            assertThat(sale.getEndTime()).isEqualTo(saleDTO.getEndTime());
            assertThat(sale.getStatus()).isEqualTo(saleDTO.getStatus());
            assertThat(sale.getTable().getId()).isEqualTo(saleDTO.getTableId());
            assertThat(sale.getTable().getName()).isEqualTo(saleDTO.getTableName());
            assertThat(sale.getNote()).isEqualTo(saleDTO.getNote());
            assertThat(sale.getDollarToLocalCurrencyExchange()).isEqualTo(saleDTO.getDollarToLocalCurrencyExchange());
            assertThat(sale.getReservation().getId()).isEqualTo(saleDTO.getReservationId());
    }

    @Test
    void shouldPartiallyMapSaleDTOtoSaleEntity() {
        // Create a Table and a Reservation to be used in the Sale
        Table table = utils.createTable(null);
        Reservation reservation = utils.createReservation(null, null);

        SaleDTO saleDTO = utils.createSaleDTO(table, reservation);
        SaleDTO saleDTOTest = utils.createSaleDTO(table, reservation);
        // Create a paymentOption to be used in the Sale
        PaymentOption paymentOption = utils.createPaymentOption(null);

        Sale sale = saleMapper.toEntity(saleDTO, table, reservation, paymentOption);

        // Change the table on the saleDTO and update the sale
        saleDTOTest.setTableId(2L);
        // Change the reservation on the saleDTO and update the sale
        saleDTOTest.setReservationId(2L);
        saleMapper.updateModel(saleDTOTest, sale, paymentOption);

        // Assert that saleDTO is equal to sale after the update (reservation and table should not be updated)
        assertThat(sale.getId()).isEqualTo(saleDTO.getId());
        assertThat(sale.getClientQuantity()).isEqualTo(saleDTO.getClientQuantity());
        assertThat(sale.getStartTime()).isEqualTo(saleDTO.getStartTime());
        assertThat(sale.getEndTime()).isEqualTo(saleDTO.getEndTime());
        assertThat(sale.getStatus()).isEqualTo(saleDTO.getStatus());
        assertThat(sale.getTable().getId()).isEqualTo(saleDTO.getTableId());
        assertThat(sale.getTable().getName()).isEqualTo(saleDTO.getTableName());
        assertThat(sale.getNote()).isEqualTo(saleDTO.getNote());
        assertThat(sale.getDollarToLocalCurrencyExchange()).isEqualTo(saleDTO.getDollarToLocalCurrencyExchange());
        assertThat(sale.getReservation().getId()).isEqualTo(saleDTO.getReservationId());
        assertThat(sale.getPaymentOption().getId()).isEqualTo(paymentOption.getId());

        // Check that the table and reservation are not updated
        assertThat(sale.getTable().getId()).isNotEqualTo(saleDTOTest.getTableId());
        assertThat(sale.getReservation().getId()).isNotEqualTo(saleDTOTest.getReservationId());


        // Now lets check that all the other attributes can be updated
        // Change the clientQuantity on the saleDTO and update the sale
        saleDTO.setClientQuantity(2);
        // Change the startTime on the saleDTO and update the sale
        saleDTO.setStartTime(Date.from(new Date().toInstant().plusSeconds(1000)));
        // Change the endTime on the saleDTO and update the sale
        saleDTO.setEndTime(Date.from(new Date().toInstant().plusSeconds(2000)));
        // Change the status on the saleDTO and update the sale
        saleDTO.setStatus(SaleStatics.Status.canceled);
        // Change the note on the saleDTO and update the sale
        saleDTO.setNote(" TEsting note");
        // Change the dollarToLocalCurrencyExchange on the saleDTO and update the sale
        saleDTO.setDollarToLocalCurrencyExchange(BigDecimal.valueOf(2.5));

        saleMapper.updateModel(saleDTO, sale, paymentOption);

        // Assert that saleDTO is equal to sale after the update (reservation and table should not be updated)
        assertThat(sale.getId()).isEqualTo(saleDTO.getId());
        assertThat(sale.getClientQuantity()).isEqualTo(saleDTO.getClientQuantity());
        assertThat(sale.getEndTime()).isEqualTo(saleDTO.getEndTime());
        assertThat(sale.getStatus()).isEqualTo(saleDTO.getStatus());
        assertThat(sale.getTable().getId()).isEqualTo(saleDTO.getTableId());
        assertThat(sale.getTable().getName()).isEqualTo(saleDTO.getTableName());
        assertThat(sale.getNote()).isEqualTo(saleDTO.getNote());
        assertThat(sale.getDollarToLocalCurrencyExchange()).isEqualTo(saleDTO.getDollarToLocalCurrencyExchange());
        assertThat(sale.getReservation().getId()).isEqualTo(saleDTO.getReservationId());
        
        assertThat(sale.getStartTime()).isNotEqualTo(saleDTO.getStartTime()); // The startTime should not be updated
        
        

    }
}
