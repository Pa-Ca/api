package com.paca.paca.sale;

import com.paca.paca.ServiceTest;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.service.SaleProductService;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SaleProductServiceTest extends ServiceTest {

    @InjectMocks
    private SaleProductService saleProductService;

    @Test
    void shouldSave() throws Exception {
        Sale sale = utils.createSale(null, null, null);
        Product product = utils.createProduct(null);
        SaleProduct saleProduct = utils.createSaleProduct(sale, product);
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(saleProduct);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(saleProductMapper.toEntity(any(), any(), any())).thenReturn(saleProduct);
        when(saleProductRepository.save(any())).thenReturn(saleProduct);
        when(saleProductMapper.toDTO(any())).thenReturn(saleProductDTO);

        SaleProductDTO response = saleProductService.save(saleProductDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleNotExistingInSave() {
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleProductService.save(saleProductDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + saleProductDTO.getSaleId() + " does not exist", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleIsNoOutGoingInSave() {
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null);
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus((short) (SaleStatics.Status.ONGOING + 1));

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleProductService.save(saleProductDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + saleProductDTO.getSaleId() + " is not ongoing", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 47);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToProductNotExistingInSave() {
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null);
        Sale sale = utils.createSale(null, null, null);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleProductService.save(saleProductDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Product with id " + saleProductDTO.getProductId() + " does not exist", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 25);
        }
    }

    @Test
    void shouldDelete() {
        SaleProduct saleProduct = utils.createSaleProduct(null, null);

        when(saleProductRepository.findById(1L)).thenReturn(Optional.of(saleProduct));

        saleProductService.delete(1L);

        verify(saleProductRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleProductNotExistingInDelete(){
        when(saleProductRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        try {
            saleProductService.delete(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale product with id " + 1L + " does not exist", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetForbiddenExceptionDueToSaleProductNotOngoingInDelete() {
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus((short) (SaleStatics.Status.ONGOING + 1));

        when(saleProductRepository.findById(anyLong())).thenReturn(Optional.of(saleProduct));

        try {
            saleProductService.delete(saleProduct.getId());
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is not ongoing", e.getMessage());
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 47);
        }
    }

    @Test
    void shouldUpdate() {
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null);

        when(saleProductRepository.save(any())).thenReturn(saleProduct);
        when(saleProductRepository.findById(anyLong())).thenReturn(Optional.of(saleProduct));
        when(saleProductMapper.updateModel(any(), any())).thenReturn(saleProduct);
        when(saleProductMapper.toDTO(any())).thenReturn(saleProductDTO);

        SaleProductDTO response = saleProductService.update(1L, saleProductDTO);

        assertThat(response).isNotNull();

    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleProductNotExistingInUpdate() {
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null);
        when(saleProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleProductService.update(1L, saleProductDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale product with id " + 1L + " does not exist", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetForbiddenExceptionDueToSaleIsNoOutGoingInUpdate() {
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null);
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus((short) (SaleStatics.Status.ONGOING + 1));

        when(saleProductRepository.findById(anyLong())).thenReturn(Optional.of(saleProduct));

        try {
            saleProductService.update(1L, saleProductDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals("Sale with id " + 1L + " is not ongoing", e.getMessage());
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 47);
        }
    }

}
