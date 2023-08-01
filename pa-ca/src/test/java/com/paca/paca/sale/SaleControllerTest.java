package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.sale.controller.SaleController;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.statics.SaleStatics;

import org.hamcrest.CoreMatchers;
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

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { SaleController.class })
public class SaleControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private SaleService saleService;
    

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldSave() throws Exception{
        
        SaleDTO saleDto = utils.createSaleDTO(null, null);
        saleDto.setReservationId(null);
        // Create a SaleInfoDTO

        SaleInfoDTO dto = SaleInfoDTO.builder()
            .sale(saleDto)
            .build();

        Business business = utils.createBusiness(null);

        when(saleService.save(any(SaleDTO.class))).thenReturn(dto);
        when(tableRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(post(SaleStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.id",
                        CoreMatchers.is(saleDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.clientQuantity",
                        CoreMatchers.is(saleDto.getClientQuantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.startTime",
                        CoreMatchers.is(saleDto.getStartTime().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.status",
                        CoreMatchers.is(saleDto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.tableId",
                        CoreMatchers.is(saleDto.getTableId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.tableName",
                        CoreMatchers.is(saleDto.getTableName())));

    }

    @Test
    void shouldGetForbiddenDueToInvalidRoleInSave() throws Exception{

        utils.setAuthorities("client");

        mockMvc.perform(post(SaleStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("user");
        mockMvc.perform(post(SaleStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfSaleInSave() throws Exception{

        SaleDTO dto = utils.createSaleDTO(null, null);

        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(post(SaleStatics.Endpoint.PATH)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }


    @Test
    void shouldUpdate()throws Exception{

        SaleDTO saleDto = utils.createSaleDTO(null, null);

        // Create a SaleInfoDTO

        SaleInfoDTO dto = SaleInfoDTO.builder()
            .sale(saleDto)
            .build();

        Business business = utils.createBusiness(null);
        
        utils.setAuthorities("business");

        when(saleService.update(anyLong(), any(SaleDTO.class))).thenReturn(dto);
        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        mockMvc.perform(put(SaleStatics.Endpoint.PATH.concat("/" + saleDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.id",
                        CoreMatchers.is(saleDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.clientQuantity",
                        CoreMatchers.is(saleDto.getClientQuantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.startTime",
                        CoreMatchers.is(saleDto.getStartTime().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.status",
                        CoreMatchers.is(saleDto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.tableId",
                        CoreMatchers.is(saleDto.getTableId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sale.tableName",
                        CoreMatchers.is(saleDto.getTableName())));
    } 

    @Test
    void shouldGetForbiddenDueToInvalidRoleInUpdate()throws Exception{
        SaleDTO dto = utils.createSaleDTO(null, null);
        
        utils.setAuthorities("client");


        mockMvc.perform(put(SaleStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfSaleInUpdate()throws Exception{
        SaleDTO dto = utils.createSaleDTO(null, null);

        Business business = utils.createBusiness(null);
        
        utils.setAuthorities("business");

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        mockMvc.perform(put(SaleStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldDelete()throws Exception{

        Sale Sale = utils.createSale(null, null, null);
        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        doNothing().when(saleService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(SaleStatics.Endpoint.PATH.concat("/" + Sale.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    } 

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfSaleInDelete()throws Exception{

        Sale Sale = utils.createSale(null, null, null);
        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        doNothing().when(saleService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(SaleStatics.Endpoint.PATH.concat("/" + Sale.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldGetForbiddenDueToInvalidRoleInDelete()throws Exception{

        Sale Sale = utils.createSale(null, null, null);
        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("client");

        mockMvc.perform(delete(SaleStatics.Endpoint.PATH.concat("/" + Sale.getId())))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
        
    } 

    @Test
    void shouldClearSaleProducts()throws Exception{

        Sale Sale = utils.createSale(null, null, null);
        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        doNothing().when(saleService).clearSaleProducts(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(SaleStatics.Endpoint.PATH.concat("/" + Sale.getId() + "/clear"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    } 

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfSaleInClearSaleProducts()throws Exception{

        Sale Sale = utils.createSale(null, null, null);
        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(delete(SaleStatics.Endpoint.PATH.concat("/" + Sale.getId() + "/clear"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldGetForbiddenDueToInvalidRoleInClearSaleProducts()throws Exception{

        Sale Sale = utils.createSale(null, null, null);
        Business business = utils.createBusiness(null);

        when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("client");

        mockMvc.perform(delete(SaleStatics.Endpoint.PATH.concat("/" + Sale.getId() + "/clear")))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
        
    } 


    

}
