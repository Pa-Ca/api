package com.paca.paca.promotion;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.promotion.service.PromotionService;
import com.paca.paca.promotion.statics.PromotionStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.promotion.controller.PromotionController;

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
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { PromotionController.class })
public class PromotionControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PromotionService promotionService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetPromotionList() throws Exception {
        ArrayList<PromotionDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createPromotionDTO(null));
        PromotionListDTO promotionListDTO = PromotionListDTO.builder().promotions(dtoList).build();

        when(promotionService.getAll()).thenReturn(promotionListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(PromotionStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(PromotionStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetPromotionList() throws Exception {
        ArrayList<PromotionDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createPromotionDTO(null));
        PromotionListDTO promotionListDTO = PromotionListDTO.builder().promotions(dtoList).build();

        when(promotionService.getAll()).thenReturn(promotionListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(PromotionStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.promotions", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSavePromotion() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionService.save(any(PromotionDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(post(PromotionStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSavePromotion() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionService.save(any(PromotionDTO.class))).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(PromotionStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSavePromotion() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionService.save(any(PromotionDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(PromotionStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.branchId", CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.disabled", CoreMatchers.is(dto.getDisabled())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())));
    }

    @Test
    public void shouldGetNoContentInGetPromotionById() throws Exception {
        when(promotionService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(get(PromotionStatics.Endpoint.PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetPromotionById() throws Exception {
        PromotionDTO dto = utils.createPromotionDTO(null);

        when(promotionService.getById(anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(get(PromotionStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.branchId", CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.disabled", CoreMatchers.is(dto.getDisabled())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdatePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        utils.setAuthorities("client");

        mockMvc.perform(put(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInUpdatePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);
        Business business = utils.createBusiness(null);

        when(promotionService.update(anyLong(), any(PromotionDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(put(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdatePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        Business business = utils.createBusiness(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionService.update(anyLong(), any(PromotionDTO.class))).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdatePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        Business business = utils.createBusiness(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionService.update(anyLong(), any(PromotionDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.branchId", CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.disabled", CoreMatchers.is(dto.getDisabled())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeletePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(delete(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeletePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(delete(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeletePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        doThrow(new NoContentException("message", 0)).when(promotionService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeletePromotionById() throws Exception {
        Promotion promotion = utils.createPromotion(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(promotionRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(promotionService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(PromotionStatics.Endpoint.PATH.concat("/" + promotion.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    
}
