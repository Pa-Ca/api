package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.paca.paca.branch.controller.TableController;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Table;

import com.paca.paca.branch.service.TableService;
import com.paca.paca.branch.statics.TableStatics;

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
@WebMvcTest(controllers = { TableController.class })
public class TableControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TableService tableService;
    

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldSave() throws Exception{
        
        TableDTO dto = utils.createTableDTO(null);

        Business business = utils.createBusiness(null);

        when(tableService.save(any(TableDTO.class))).thenReturn(dto);
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(post(TableStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", 
                        CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted",
                        CoreMatchers.is(dto.isDeleted())));

    }

    @Test
    void shouldGetForbiddenDueToInvalidRoleInSave() throws Exception{

        utils.setAuthorities("client");

        mockMvc.perform(post(TableStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("user");
        mockMvc.perform(post(TableStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfBranchInSave() throws Exception{

        TableDTO dto = utils.createTableDTO(null);

        Business business = utils.createBusiness(null);

        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(post(TableStatics.Endpoint.PATH)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }


    @Test
    void shouldUpdate()throws Exception{
        TableDTO dto = utils.createTableDTO(null);

        Business business = utils.createBusiness(null);
        
        utils.setAuthorities("business");

        when(tableService.update(anyLong(), any(TableDTO.class))).thenReturn(dto);
        when(tableRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        mockMvc.perform(put(TableStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", 
                        CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted",
                        CoreMatchers.is(dto.isDeleted())));
    } 

    @Test
    void shouldGetForbiddenDueToInvalidRoleInUpdate()throws Exception{
        TableDTO dto = utils.createTableDTO(null);
        
        utils.setAuthorities("client");


        mockMvc.perform(put(TableStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfTableInUpdate()throws Exception{
        TableDTO dto = utils.createTableDTO(null);

        Business business = utils.createBusiness(null);
        
        utils.setAuthorities("business");

        when(tableRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        mockMvc.perform(put(TableStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldDelete()throws Exception{

        Table Table = utils.createTable(null);
        Business business = utils.createBusiness(null);

        when(tableRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
        doNothing().when(tableService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(TableStatics.Endpoint.PATH.concat("/" + Table.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    } 

    @Test
    void shouldGetForbiddenDueToBusinessNotOwnerOfTableInDelete()throws Exception{

        Table Table = utils.createTable(null);
        Business business = utils.createBusiness(null);

        when(tableRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("business");

        mockMvc.perform(delete(TableStatics.Endpoint.PATH.concat("/" + Table.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    } 

    @Test
    void shouldGetForbiddenDueToInvalidRoleInDelete()throws Exception{

        Table Table = utils.createTable(null);
        Business business = utils.createBusiness(null);

        when(tableRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);
        when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

        utils.setAuthorities("client");

        mockMvc.perform(delete(TableStatics.Endpoint.PATH.concat("/" + Table.getId())))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
        
    } 


    

}
