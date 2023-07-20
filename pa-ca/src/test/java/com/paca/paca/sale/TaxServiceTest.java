package com.paca.paca.sale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.service.TaxService;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.utils.TestUtils;

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
    void shouldSave() {
        TaxDTO  TaxDTO = utils.createTaxDTO(null);
        Tax Tax = utils.createTax(null);
        Sale sale = utils.createSale(null, null, null);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(taxRepository.save(any())).thenReturn(Tax);
        when(taxMapper.toDTO(any())).thenReturn(TaxDTO);
        
        TaxDTO  response = taxService.save(TaxDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInSave(){
        TaxDTO  TaxDTO = utils.createTaxDTO(null);
        // Set the id of the sale to 1
        TaxDTO.setSaleId(1L);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            taxService.save(TaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
            //
        }
    }

    @Test
    void shouldUpdate(){
        TaxDTO taxDTO = utils.createTaxDTO(null);
        Tax tax = utils.createTax(null);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));
        when(taxRepository.save(any())).thenReturn(tax);
        when(taxMapper.toDTO(any())).thenReturn(taxDTO);
        when(taxMapper.updateModel(any(), any())).thenReturn(tax);
        
        TaxDTO  response = taxService.update(1L ,taxDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToTaxeNotExistingInUpdate(){
        TaxDTO  TaxDTO = utils.createTaxDTO(null);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            taxService.update(1L, TaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 52);
            //
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToInvalidTaxTypeInUpdate(){
        TaxDTO  TaxDTO = utils.createTaxDTO(null);
        Tax tax = utils.createTax(null);
        // Set the type of the tax to 1
        int invalidTaxType = 42069;
        TaxDTO.setType(invalidTaxType);
        

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));
        try {
            taxService.update(1L, TaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid tax type:" + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 53);
            //
        }
    }


    @Test
    void shouldDelete(){
        Tax tax = utils.createTax(null);

        when(taxRepository.findById(1L)).thenReturn(Optional.of(tax));

        taxService.delete(1L);

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