package com.paca.paca.sale;

import com.paca.paca.ServiceTest;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.SaleTax;
import com.paca.paca.sale.dto.SaleTaxDTO;
import com.paca.paca.sale.service.SaleTaxService;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.exception.exceptions.BadRequestException;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SaleTaxServiceTest extends ServiceTest {

    @InjectMocks
    private SaleTaxService saleTaxService;

    @Test
    void shouldSave() {
        Tax tax = utils.createTax();
        Sale sale = utils.createSale(null, null, null);
        SaleTax saleTax = utils.createSaleTax(sale, tax);
        SaleTaxDTO saleTaxDTO = utils.createSaleTaxDTO(saleTax);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(taxMapper.toEntity(any(TaxDTO.class))).thenReturn(tax);
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);
        when(saleTaxRepository.save(any(SaleTax.class))).thenReturn(saleTax);
        when(taxMapper.toDTO(any(Tax.class))).thenReturn(saleTaxDTO.getTax());

        TaxDTO response = saleTaxService.save(saleTaxDTO);

        assertThat(response).isEqualTo(saleTaxDTO.getTax());
    }

    @Test
    void shouldGetNotFoundExceptionDueToSaleNotExistingInSave() {
        SaleTaxDTO saleTaxDTO = utils.createSaleTaxDTO(null);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleTaxService.save(saleTaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals("Sale with id " + saleTaxDTO.getSaleId() + " does not exists", e.getMessage());
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 42);
        }

    }

    @Test
    void shouldGetBadRequestExceptionDueToInvalidTaxTypeInSave() {
        Sale sale = utils.createSale(null, null, null);
        SaleTaxDTO saleTaxDTO = utils.createSaleTaxDTO(null);
        Short invalidTaxType = 1000;
        saleTaxDTO.getTax().setType(invalidTaxType);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleTaxService.save(saleTaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid tax type: " + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 51);
        }
    }

    @Test
    void shouldUpdate() {
        Tax tax = utils.createTax();
        TaxDTO dto = utils.createTaxDTO(tax);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));
        when(taxMapper.updateModel(any(TaxDTO.class), any(Tax.class))).thenReturn(tax);
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);
        when(taxMapper.toDTO(any(Tax.class))).thenReturn(dto);

        TaxDTO response = saleTaxService.update(tax.getId(), dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundExceptionDueToSaleTaxNotExistingInUpdate() {
        TaxDTO dto = utils.createTaxDTO(null);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleTaxService.update(1L, dto);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 52);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToDefaultInvalidTaxTypeInUpdate() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);
        Short invalidTaxType = (short) 696969;
        taxDTO.setType(invalidTaxType);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

        try {
            saleTaxService.update(1L, taxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid tax type:" + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 51);
        }
    }

    @Test
    void shouldDelete() {
        Tax tax = utils.createTax();

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

        saleTaxService.delete(tax.getId());

        verify(taxRepository, times(1)).deleteById(tax.getId());
    }

    @Test
    void shouldGetNotFoundExceptionDueToSaleTaxNotExistingInDelete(){
        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleTaxService.delete(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 52);
        }
    }

}