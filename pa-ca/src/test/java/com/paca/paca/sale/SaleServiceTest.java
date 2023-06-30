package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.exception.exceptions.NoContentException;


import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;
    
    @Mock
    private SaleMapper saleMapper;
    
    @Mock
    private TaxMapper taxMapper;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private TaxRepository taxRepository;

    @Mock
    private SaleProductRepository saleProductRepository;

    @Mock
    private SaleProductMapper saleProductMapper;

    
    @InjectMocks
    private SaleService saleService;

    private TestUtils utils = TestUtils.builder().build();
    

    @Test
    void shouldGetTaxesBySaleId() {
        
        List<Tax> taxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));
        Sale sale = utils.createSale(null, null);

        when(taxRepository.findAllBySaleId(any())).thenReturn(taxes);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        List<TaxDTO> responseDTO = saleService.getTaxesBySaleId( 1L);

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInGetTaxesBySaleId(){
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            saleService.getTaxesBySaleId(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldDeleteSalebyId(){

        Sale sale = utils.createSale(null, null);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        saleService.delete(1L);
        
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInDelete(){
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            saleService.delete(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    } 

    @Test
    void shouldGetSaleProductsbySaleId(){
        Sale sale = utils.createSale(null, null);

        List<SaleProduct> saleProducts = TestUtils.castList(SaleProduct.class, Mockito.mock(List.class));

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleProductRepository.findAllBySaleId(1L)).thenReturn(saleProducts);
        

        List<SaleProductDTO> saleProductList = saleService.getSaleProductsbySaleId(1L);

        assertThat(saleProductList).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInGetSaleProductsbySaleId(){
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            saleService.getSaleProductsbySaleId(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldUpdate(){
        SaleDTO saleDTO = utils.createSaleDTO(null, null);
        Sale sale = utils.createSale(null, null);
        List<Tax> taxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));

        when(taxRepository.findAllBySaleId(any())).thenReturn(taxes);
        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
        when(saleRepository.save(any())).thenReturn(sale);
        when(saleMapper.toDTO(any())).thenReturn(saleDTO);
        when(saleMapper.updateModel(any(), any())).thenReturn(sale);


        SaleInfoDTO saleProductDTO = saleService.update(1L, saleDTO);

        assertThat(saleProductDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInUpdate(){
        SaleDTO saleDTO = utils.createSaleDTO(null, null);

        when(saleRepository.findById(any())).thenReturn(Optional.empty());

        try {
            saleService.update(1L, saleDTO);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    // @Test
    // void shouldGetBadRequestExceptionDueToSaleBeingClosedInUpdate(){
    //     SaleDTO saleDTO = utils.createSaleDTO(null, null);
    //     Sale sale = utils.createSale(null, null);

    //     when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
    //     when(saleRepository.save(any())).thenReturn(sale);
    //     when(saleMapper.toDTO(any())).thenReturn(saleDTO);
    //     when(saleMapper.updateModel(any(), any())).thenReturn(sale);
    // }
}
