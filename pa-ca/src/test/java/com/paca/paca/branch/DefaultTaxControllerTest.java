package com.paca.paca.branch;

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
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.service.SaleService;
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
@WebMvcTest(controllers = { DefaultTaxController.class })
public class DefaultTaxControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private  DefaultTaxService defaultTaxService;


    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldSave() throws Exception {
        
        DefaultTaxDTO dto = utils.createDefaultTaxDTO(null);

        Business business = utils.createBusiness(null);

        when(defaultTaxService.save(any(DefaultTaxDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(post(DefaultTaxStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", 
                        CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", 
                        CoreMatchers.is(dto.getType())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value",
                        CoreMatchers.is(dto.getValue().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())));

    }

    @Test
    void shouldGetForbiddenDueToInvalidRoleInSave() throws Exception{

        utils.setAuthorities("client");

        mockMvc.perform(post(DefaultTaxStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("user");
        mockMvc.perform(post(DefaultTaxStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfDefaultTaxInSave() throws Exception{

        DefaultTaxDTO dto = utils.createDefaultTaxDTO(null);

        Business business = utils.createBusiness(null);

        when(defaultTaxService.save(any(DefaultTaxDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(post(DefaultTaxStatics.Endpoint.PATH)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }


    @Test
    void shouldUpdate()throws Exception{
        DefaultTaxDTO dto = utils.createDefaultTaxDTO(null);

        Business business = utils.createBusiness(null);
        
        utils.setAuthorities("business");

        when(defaultTaxService.update(anyLong(), any(DefaultTaxDTO.class))).thenReturn(dto);
        when(defaultTaxRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        mockMvc.perform(put(DefaultTaxStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", 
                        CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", 
                        CoreMatchers.is(dto.getType())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value",
                        CoreMatchers.is(dto.getValue().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())));
    } 


    @Test
    void shouldGetForbiddenDueToInvalidRoleInUpdate()throws Exception{
        DefaultTaxDTO dto = utils.createDefaultTaxDTO(null);
        
        utils.setAuthorities("client");


        mockMvc.perform(put(DefaultTaxStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfDefaultTaxInUpdate()throws Exception{
        DefaultTaxDTO dto = utils.createDefaultTaxDTO(null);

        Business business = utils.createBusiness(null);
        
        utils.setAuthorities("business");

        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        when(defaultTaxRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(put(DefaultTaxStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldDelete()throws Exception{

        DefaultTax defaultTax = utils.createDefaultTax(null);
        Business business = utils.createBusiness(null);

        when(defaultTaxRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        doNothing().when(defaultTaxService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(DefaultTaxStatics.Endpoint.PATH.concat("/" + defaultTax.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    } 

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfDefaultTaxInDelete()throws Exception{

        DefaultTax defaultTax = utils.createDefaultTax(null);
        Business business = utils.createBusiness(null);

        when(defaultTaxRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        doNothing().when(defaultTaxService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(DefaultTaxStatics.Endpoint.PATH.concat("/" + defaultTax.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldGetForbiddenDueToInvalidRoleInDelete()throws Exception{

        DefaultTax defaultTax = utils.createDefaultTax(null);
        Business business = utils.createBusiness(null);

        when(defaultTaxRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("client");

        mockMvc.perform(delete(DefaultTaxStatics.Endpoint.PATH.concat("/" + defaultTax.getId())))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
        
    } 


}
