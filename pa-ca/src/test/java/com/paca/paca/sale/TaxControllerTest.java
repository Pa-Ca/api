package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.sale.controller.TaxController;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.service.TaxService;
import com.paca.paca.sale.statics.TaxStatics;

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
        void shouldSave() throws Exception {

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

        @Test
        void shouldGetForbiddenDueToInvalidRoleInSave() throws Exception {

                utils.setAuthorities("client");

                mockMvc.perform(post(TaxStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));

                utils.setAuthorities("user");
                mockMvc.perform(post(TaxStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldGetForbiddenDueToBusinessNotOwnerOfSaleInSave() throws Exception {

                TaxDTO dto = utils.createTaxDTO(null);

                Business business = utils.createBusiness(null);

                when(taxService.save(any(TaxDTO.class))).thenReturn(dto);
                when(saleRepository.existsByIdAndTable_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

                utils.setAuthorities("business");

                mockMvc.perform(post(TaxStatics.Endpoint.PATH)
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldUpdate() throws Exception {
                TaxDTO dto = utils.createTaxDTO(null);

                Business business = utils.createBusiness(null);

                utils.setAuthorities("business");

                when(taxService.update(anyLong(), any(TaxDTO.class))).thenReturn(dto);
                when(taxRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

                mockMvc.perform(put(TaxStatics.Endpoint.PATH.concat("/" + dto.getId()))
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

        @Test
        void shouldGetForbiddenDueToInvalidRoleInUpdate() throws Exception {
                TaxDTO dto = utils.createTaxDTO(null);

                utils.setAuthorities("client");

                mockMvc.perform(put(TaxStatics.Endpoint.PATH.concat("/" + dto.getId()))
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldGetForbiddenDueToBusinessNotOwnerOfTaxInUpdate() throws Exception {
                TaxDTO dto = utils.createTaxDTO(null);

                Business business = utils.createBusiness(null);

                utils.setAuthorities("business");

                when(taxRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

                mockMvc.perform(put(TaxStatics.Endpoint.PATH.concat("/" + dto.getId()))
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldDelete() throws Exception {

                Tax tax = utils.createTax(null);
                Business business = utils.createBusiness(null);

                when(taxRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                doNothing().when(taxService).delete(anyLong());

                utils.setAuthorities("business");

                mockMvc.perform(delete(TaxStatics.Endpoint.PATH.concat("/" + tax.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        void shouldGetForbiddenDueToBusinessNotOwnerOfTaxInDelete() throws Exception {

                Tax tax = utils.createTax(null);
                Business business = utils.createBusiness(null);

                when(taxRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));
                doNothing().when(taxService).delete(anyLong());

                utils.setAuthorities("business");

                mockMvc.perform(delete(TaxStatics.Endpoint.PATH.concat("/" + tax.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        void shouldGetForbiddenDueToInvalidRoleInDelete() throws Exception {

                Tax tax = utils.createTax(null);
                Business business = utils.createBusiness(null);

                when(taxRepository.existsByIdAndSale_Table_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);
                when(businessRepository.findByUserEmail(anyString())).thenReturn(Optional.of(business));

                utils.setAuthorities("client");

                mockMvc.perform(delete(TaxStatics.Endpoint.PATH.concat("/" + tax.getId())))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));

        }

}
