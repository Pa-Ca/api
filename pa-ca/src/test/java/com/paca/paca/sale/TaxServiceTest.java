package com.paca.paca.sale;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.service.TaxService;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class TaxServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private TaxRepository taxRepository;

    @Mock
    private TaxMapper taxMapper;

    @InjectMocks
    private TaxService taxService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldUpdate() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));
        when(taxRepository.save(any())).thenReturn(tax);
        when(taxMapper.toDTO(any())).thenReturn(taxDTO);
        when(taxMapper.updateModel(any(), any())).thenReturn(tax);

        TaxDTO response = taxService.update(1L, taxDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToTaxeNotExistingInUpdate() {
        TaxDTO TaxDTO = utils.createTaxDTO(null);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            taxService.update(1L, TaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 52);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToInvalidTaxTypeInUpdate() {
        Tax tax = utils.createTax();
        TaxDTO TaxDTO = utils.createTaxDTO(tax);
        Short invalidTaxType = Short.valueOf("42069");
        TaxDTO.setType(invalidTaxType);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

        try {
            taxService.update(1L, TaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid tax type:" + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 53);
        }
    }

    @Test
    void shouldDelete() {
        Tax tax = utils.createTax();

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

        taxService.delete(tax.getId());

        verify(taxRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldGetNoContentExcepetionDueToTaxNotExistingInDelete(){

        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            taxService.delete(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 52);
            //
        }
    }

}