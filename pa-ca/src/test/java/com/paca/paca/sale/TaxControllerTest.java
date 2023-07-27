package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.sale.controller.TaxController;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.service.TaxService;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.branch.controller.BranchController;
import com.paca.paca.branch.controller.AmenityController;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.statics.ProductStatics;

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
@WebMvcTest(controllers = { TaxController.class })
public class TaxControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TaxService taxService;
    

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void  shouldSave() throws Exception{
        
        TaxDTO dto = utils.createTaxDTO(null);

        Business business = utils.createBusiness(null);

        when(taxService.save(any(TaxDTO.class))).thenReturn(dto);
        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(post(TaxStatics.Endpoint.PATH)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.saleId",
                        CoreMatchers.is(dto.getSaleId().intValue())));
    }

    

}
