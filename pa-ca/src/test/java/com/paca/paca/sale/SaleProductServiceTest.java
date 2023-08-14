package com.paca.paca.sale;

import com.paca.paca.sale.controller.SaleProductController;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.product.service.ProductService;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.service.DefaultTaxService;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.branch.statics.DefaultTaxStatics;
import com.paca.paca.branch.statics.AmenityStatics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.service.SaleProductService;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.statics.SaleProductStatics;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.controller.BranchController;
import com.paca.paca.branch.controller.DefaultTaxController;
import com.paca.paca.branch.controller.AmenityController;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;
import java.util.ArrayList;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)

public class SaleProductServiceTest {


    @Mock
    private SaleProductRepository saleProductRepository;//
    @Mock
    private SaleRepository saleRepository;//
    @Mock
    private ProductRepository productRepository; //
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BusinessRepository businessRepository;//
    @Mock
    private UserRepository userRepository;
    @Mock
    private SaleProductMapper saleProductMapper;//

    @InjectMocks
    private SaleProductService saleProductService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldSave() throws Exception{
        
        Sale sale = utils.createSale(null, null, null);
        Product product = utils.createProduct(null);
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null);


        when(saleProductRepository.save(any())).thenReturn(saleProduct);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(saleProductMapper.toEntity(any(), any(), any())).thenReturn(saleProduct);
        when(saleProductMapper.toDTO(any())).thenReturn(saleProductDTO);


        SaleProductDTO  response = saleProductService.save(saleProductDTO);

        assertThat(response).isNotNull();

    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleNotExistingInSave(){
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null);  
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
    void shouldGetBadRequestExceptionDueToSaleIsNoOutGoingInSave(){
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null); 
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.ongoing +1);
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
    void shouldGetBadRequestExceptionDueToProductNotExistingInSave(){
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null); 
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
    void shouldDelete(){
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
    void shouldGetForbiddenExceptionDueToSaleProductNotOngoingInDelete(){

        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.ongoing +1);
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
    void shouldUpdate(){
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null);


        when(saleProductRepository.save(any())).thenReturn(saleProduct);//
        when(saleProductRepository.findById(anyLong())).thenReturn(Optional.of(saleProduct));//
        when(saleProductMapper.updateModel(any(), any())).thenReturn(saleProduct);//
        when(saleProductMapper.toDTO(any())).thenReturn(saleProductDTO);//
        
        SaleProductDTO  response = saleProductService.update(1L, saleProductDTO);

        assertThat(response).isNotNull();

    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleProductNotExistingInUpdate() {
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null);
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
    void shouldGetForbiddenExceptionDueToSaleIsNoOutGoingInUpdate(){
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(null, null); 
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.ongoing +1);
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

    