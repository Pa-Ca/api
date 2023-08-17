package com.paca.paca.sale;

import com.paca.paca.sale.controller.SaleProductController;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Tax;
import com.paca.paca.branch.dto.TaxDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.product.dto.ProductListDTO;
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
import com.paca.paca.reservation.dto.ReservationInfoListDTO;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.service.SaleProductService;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.statics.SaleProductStatics;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.branch.controller.BranchController;
import com.paca.paca.branch.controller.DefaultTaxController;
import com.paca.paca.branch.controller.AmenityController;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { SaleProductController.class })
public class SaleProductControllerTest extends ControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private SaleProductService saleProductService;

        private TestUtils utils = TestUtils.builder().build();

        @Test
        void shouldSave() throws Exception {

                SaleProductDTO dto = utils.createSaleProductDTO(null, null);

                Business business = utils.createBusiness(null);

                when(saleProductService.save(any(SaleProductDTO.class))).thenReturn(dto);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);
                when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);

                utils.setAuthorities("business");

                mockMvc.perform(post(SaleProductStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                CoreMatchers.is(dto.getId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                                                CoreMatchers.is(dto.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.saleId",
                                                CoreMatchers.is(dto.getSaleId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productId",
                                                CoreMatchers.is(dto.getProductId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.amount",
                                                CoreMatchers.is(dto.getAmount().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.price",
                                                CoreMatchers.is(dto.getPrice().doubleValue())));

        }

        @Test
        void shouldGetForbiddenDueToInvalidRoleInSave() throws Exception {

                utils.setAuthorities("client");

                mockMvc.perform(post(SaleProductStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));

                utils.setAuthorities("user");
                mockMvc.perform(post(SaleProductStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldGetForbiddenDueToBusinessNotOwnerOfSaleProductInSave() throws Exception {

                SaleProductDTO dto = utils.createSaleProductDTO(null, null);

                Business business = utils.createBusiness(null);

                when(saleProductService.save(any(SaleProductDTO.class))).thenReturn(dto);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(false);
                when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);

                utils.setAuthorities("business");

                mockMvc.perform(post(SaleProductStatics.Endpoint.PATH)
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldUpdate() throws Exception {

                SaleProductDTO dto = utils.createSaleProductDTO(null, null);
                Business business = utils.createBusiness(null);

                utils.setAuthorities("business");

                when(saleProductService.update(anyLong(), any(SaleProductDTO.class))).thenReturn(dto);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                when(saleProductRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);

                mockMvc.perform(put(SaleProductStatics.Endpoint.PATH.concat("/" + dto.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                CoreMatchers.is(dto.getId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                                                CoreMatchers.is(dto.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.saleId",
                                                CoreMatchers.is(dto.getSaleId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productId",
                                                CoreMatchers.is(dto.getProductId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.amount",
                                                CoreMatchers.is(dto.getAmount().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.price",
                                                CoreMatchers.is(dto.getPrice().doubleValue())));
        }

        @Test
        void shouldGetForbiddenDueToInvalidRoleInUpdate() throws Exception {
                SaleProductDTO dto = utils.createSaleProductDTO(null, null);

                utils.setAuthorities("client");

                mockMvc.perform(put(SaleProductStatics.Endpoint.PATH.concat("/" + dto.getId()))
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldGetForbiddenDueToBusinessNotOwnerOfSaleProductInUpdate() throws Exception {
                SaleProductDTO dto = utils.createSaleProductDTO(null, null);

                Business business = utils.createBusiness(null);

                utils.setAuthorities("business");

                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                when(saleProductRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(false);

                mockMvc.perform(put(SaleProductStatics.Endpoint.PATH.concat("/" + dto.getId()))
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldDelete() throws Exception {

                SaleProduct saleProduct = utils.createSaleProduct(null, null);
                Business business = utils.createBusiness(null);

                when(saleProductRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                doNothing().when(saleProductService).delete(anyLong());

                utils.setAuthorities("business");

                mockMvc.perform(delete(SaleProductStatics.Endpoint.PATH.concat("/" + saleProduct.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        void shouldGetForbiddenDueToBusinessNotOwnerOfSaleProductInDelete() throws Exception {

                SaleProduct saleProduct = utils.createSaleProduct(null, null);
                Business business = utils.createBusiness(null);

                when(saleProductRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(false);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                doNothing().when(saleProductService).delete(anyLong());

                utils.setAuthorities("business");

                mockMvc.perform(delete(SaleProductStatics.Endpoint.PATH.concat("/" + saleProduct.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldGetForbiddenDueToInvalidRoleInDelete() throws Exception {

                SaleProduct saleProduct = utils.createSaleProduct(null, null);
                Business business = utils.createBusiness(null);

                when(saleProductRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(false);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

                utils.setAuthorities("client");

                mockMvc.perform(delete(SaleProductStatics.Endpoint.PATH.concat("/" + saleProduct.getId())))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));

        }

}
