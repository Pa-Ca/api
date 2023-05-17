package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.branch.statics.AmenityStatics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.branch.controller.BranchController;
import com.paca.paca.branch.controller.AmenityController;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;

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

import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { BranchController.class, AmenityController.class })
public class BranchControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BranchService branchService;

    @MockBean
    private AmenityService amenityService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetBranchList() throws Exception {
        ArrayList<BranchDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createBranchDTO(null));
        BranchListDTO branchListDTO = BranchListDTO.builder().branches(dtoList).build();

        when(branchService.getAll()).thenReturn(branchListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetBranchList() throws Exception {
        ArrayList<BranchDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createBranchDTO(null));
        BranchListDTO branchListDTO = BranchListDTO.builder().branches(dtoList).build();

        when(branchService.getAll()).thenReturn(branchListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.branches", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveBranch() throws Exception {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchService.save(any(BranchDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSaveBranch() throws Exception {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchService.save(any(BranchDTO.class))).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSaveBranch() throws Exception {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchService.save(any(BranchDTO.class))).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveBranch() throws Exception {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchService.save(any(BranchDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.businessId", CoreMatchers.is(dto.getBusinessId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(dto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinates", CoreMatchers.is(dto.getCoordinates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overview", CoreMatchers.is(dto.getOverview())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score", CoreMatchers.is((double) dto.getScore())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity", CoreMatchers.is(dto.getCapacity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationPrice",
                        CoreMatchers.is((double) dto.getReservationPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reserveOff", CoreMatchers.is(dto.getReserveOff())));
    }

    @Test
    public void shouldGetNoContentInGetBranchById() throws Exception {
        when(branchService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetBranchById() throws Exception {
        BranchDTO dto = utils.createBranchDTO(null);

        when(branchService.getById(anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.businessId", CoreMatchers.is(dto.getBusinessId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(dto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinates", CoreMatchers.is(dto.getCoordinates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overview", CoreMatchers.is(dto.getOverview())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score", CoreMatchers.is((double) dto.getScore())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity", CoreMatchers.is(dto.getCapacity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationPrice",
                        CoreMatchers.is((double) dto.getReservationPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reserveOff", CoreMatchers.is(dto.getReserveOff())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdateBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        utils.setAuthorities("client");

        mockMvc.perform(put(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInUpdateBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);
        Business business = utils.createBusiness(null);

        when(branchService.update(anyLong(), any(BranchDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(put(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdateBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        Business business = utils.createBusiness(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchService.update(anyLong(), any(BranchDTO.class))).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        Business business = utils.createBusiness(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchService.update(anyLong(), any(BranchDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.businessId", CoreMatchers.is(dto.getBusinessId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(dto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinates", CoreMatchers.is(dto.getCoordinates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overview", CoreMatchers.is(dto.getOverview())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score", CoreMatchers.is((double) dto.getScore())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity", CoreMatchers.is(dto.getCapacity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationPrice",
                        CoreMatchers.is((double) dto.getReservationPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reserveOff", CoreMatchers.is(dto.getReserveOff())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);
        doThrow(new NoContentException("message", 0)).when(branchService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteBranchById() throws Exception {
        Branch branch = utils.createBranch(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(branchService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGetNoContentInGetProductSubCategories() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getProductSubCategories(anyLong(), anyLong()))
                .thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/product-category/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetProductSubCategories() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ProductSubCategoryDTO> subCategoryDTOList = new ArrayList<>();
        subCategoryDTOList.add(utils.createProductSubCategoryDTO(null));
        ProductSubCategoryListDTO subCategoryListDTO = ProductSubCategoryListDTO.builder()
                .productSubCategories(subCategoryDTOList).build();

        when(branchService.getProductSubCategories(anyLong(), anyLong())).thenReturn(subCategoryListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/product-category/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSubCategories", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetNoContentInGetProducts() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getProducts(anyLong())).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/product"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetProducts() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(utils.createProductDTO(null));
        ProductListDTO productListDTO = ProductListDTO.builder().products(productDTOList).build();

        when(branchService.getProducts(anyLong())).thenReturn(productListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/product"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetNoContentInGetPromotions() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getPromotions(anyLong())).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/promotion"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetPromotions() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<PromotionDTO> promotionDTOList = new ArrayList<>();
        promotionDTOList.add(utils.createPromotionDTO(null));
        PromotionListDTO promotionListDTO = PromotionListDTO.builder().promotions(promotionDTOList).build();

        when(branchService.getPromotions(anyLong())).thenReturn(promotionListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/promotion"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.promotions", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReservations() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(branchService.getReservations(anyLong())).thenReturn(reservationListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetReservations() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(branchService.getReservations(anyLong())).thenReturn(reservationListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetReservations() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getReservations(anyLong())).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReservations() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(branchService.getReservations(anyLong())).thenReturn(reservationListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReservationsByDate() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(branchService.getReservationsByDate(anyLong(), any(Date.class))).thenReturn(reservationListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetReservationsByDate() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(branchService.getReservationsByDate(anyLong(), any(Date.class))).thenReturn(reservationListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetReservationsByDate() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getReservationsByDate(anyLong(), any(Date.class)))
                .thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReservationsByDate() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(branchService.getReservationsByDate(anyLong(), any(Date.class))).thenReturn(reservationListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetFavoriteClients() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(branchService.getFavoriteClients(anyLong())).thenReturn(clientListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/favorite-clients"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetFavoriteClients() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(branchService.getFavoriteClients(anyLong())).thenReturn(clientListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/favorite-clients"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetFavoriteClients() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getFavoriteClients(anyLong())).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/favorite-clients"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetFavoriteClients() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(branchService.getFavoriteClients(anyLong())).thenReturn(clientListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/favorite-clients"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetNoContentInGetReviews() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        when(branchService.getReviews(anyLong())).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/review"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReviews() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<ReviewDTO> reviewDTOList = new ArrayList<>();
        reviewDTOList.add(utils.createReviewDTO(null));
        ReviewListDTO reviewListDTO = ReviewListDTO.builder().reviews(reviewDTOList).build();

        when(branchService.getReviews(anyLong())).thenReturn(reviewListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/review"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviews", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetNoContentInGetAmenityListByBranchId() throws Exception {
        when(amenityService.getAllByBranchId(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/amenity"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetAmenityListByBranchId() throws Exception {
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.getAllByBranchId(anyLong())).thenReturn(amenityListDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/amenity"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amenities", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveBranchAmenities() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.saveAllByBranchId(anyLong(), any(AmenityListDTO.class))).thenReturn(amenityListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/amenity"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenityListDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSaveBranchAmenities() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.saveAllByBranchId(anyLong(), any(AmenityListDTO.class)))
                .thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/amenity"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenityListDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveBranchAmenities() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.saveAllByBranchId(anyLong(), any(AmenityListDTO.class))).thenReturn(amenityListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(post(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/amenity"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenityListDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amenities", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteBranchAmenities() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.deleteAllByBranchId(anyLong(), any(AmenityListDTO.class))).thenReturn(amenityListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/amenity"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenityListDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteBranchAmenities() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.deleteAllByBranchId(anyLong(), any(AmenityListDTO.class)))
                .thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/amenity"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenityListDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteBranchAmenities() throws Exception {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.deleteAllByBranchId(anyLong(), any(AmenityListDTO.class))).thenReturn(amenityListDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(delete(BranchStatics.Endpoint.PATH.concat("/" + branch.getId() + "/amenity"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenityListDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amenities", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetAmenityList() throws Exception {
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.getAll()).thenReturn(amenityListDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(AmenityStatics.Endpoint.AMENITY_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amenities", CoreMatchers.hasItems()));
    }
    @Test
    public void shouldGetAmenityListByWord() throws Exception {
        ArrayList<AmenityDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createAmenityDTO(null));
        AmenityListDTO amenityListDTO = AmenityListDTO.builder().amenities(dtoList).build();

        when(amenityService.getBySearchWord(anyString())).thenReturn(amenityListDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(AmenityStatics.Endpoint.AMENITY_PATH.concat("/search/word"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amenities", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetBranchByAmenityId() throws Exception {
        ArrayList<BranchDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createBranchDTO(null));
        BranchListDTO branchListDTO = BranchListDTO.builder().branches(dtoList).build();

        when(amenityService.getAllBranches(anyLong())).thenReturn(branchListDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(AmenityStatics.Endpoint.AMENITY_PATH.concat("/1/branch"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.branches", CoreMatchers.hasItems()));
    }
    @Test
    public void shouldGetNoContentDueToMissingBranchInGetReviewsPage() throws Exception {
        when(branchService.getReviewsPage(anyLong(), anyInt(), anyInt())).thenThrow(new NoContentException("Branch with id 1 does not exists", 20));

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reviews?page=2&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Branch with id 1 does not exists")));
    }

    @Test
    public void shouldGetUnprocessableExceptionToPageLessThanZeroInGetReviewsPage() throws Exception{
        when(branchService.getReviewsPage(anyLong(), anyInt(), anyInt())).thenThrow(new UnprocessableException("Page number cannot be less than zero", 40));

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reviews?page=-1&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(40)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Page number cannot be less than zero")));
    }

    @Test
    public void shouldGetUnprocessableDueToPageSizeLessThanOneInGetReviewsPage() throws Exception{
        when(branchService.getReviewsPage(anyLong(), anyInt(), anyInt())).thenThrow(new UnprocessableException("Size cannot be less than one", 41));

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reviews?page=1&size=0"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(41)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Size cannot be less than one")));
    }

    @Test
    public void shouldGetReviewPageByBranchId() throws Exception {

        ArrayList<ReviewDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dtoList.add(utils.createReviewDTO(null));
        }

        ReviewListDTO reviewListDTO = ReviewListDTO.builder().reviews(dtoList).build();

        when(branchService.getReviewsPage(anyLong(), anyInt(), anyInt())).thenReturn(reviewListDTO);

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reviews?page=1&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviews", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetUnprocessableExceptionToPageLessThanZeroInGetBranchesPage() throws Exception {

        when(branchService.getBranchesPage(
                anyInt(),
                anyInt(),
                anyString(),
                anyBoolean(),
                anyFloat(),
                anyFloat(),
                anyFloat(),
                anyInt()
        )).thenThrow(new UnprocessableException("Page number cannot be less than zero", 40));

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/branches?page=-1&size=10&sorting_by=name&ascending=true&min_reservation_price=0&max_reservation_price=1000&min_score=0&min_capacity=0"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(40)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Page number cannot be less than zero")));
    }

    @Test
    public void shouldGetUnprocessableExceptionToPageSizeLessThanOneInGetBranchesPage() throws Exception {

        when(branchService.getBranchesPage(
                anyInt(),
                anyInt(),
                anyString(),
                anyBoolean(),
                anyFloat(),
                anyFloat(),
                anyFloat(),
                anyInt()
        )).thenThrow(new UnprocessableException("Size cannot be less than one", 41));

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/branches?page=0&size=0&sorting_by=name&ascending=true&min_reservation_price=0&max_reservation_price=1000&min_score=0&min_capacity=0"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(41)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Size cannot be less than one")));
    }

    @Test
    public void shouldGetUnprocessableExceptionDueToInvalidSortingKeyInGetBranchesPage() throws Exception {

        when(branchService.getBranchesPage(
                anyInt(),
                anyInt(),
                anyString(),
                anyBoolean(),
                anyFloat(),
                anyFloat(),
                anyFloat(),
                anyInt()
        )).thenThrow(new UnprocessableException("Sorting key is not valid", 42));

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/branches?page=0&size=0&sorting_by=meme&ascending=true&min_reservation_price=0&max_reservation_price=1000&min_score=0&min_capacity=0"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(42)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Sorting key is not valid")));
    }


    @Test
    public void shouldGetBranchesPage() throws Exception {

        ArrayList<BranchDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dtoList.add(utils.createBranchDTO(null));
        }

        BranchListDTO branchListDTO = BranchListDTO.builder().branches(dtoList).build();

        when(branchService.getBranchesPage(
                anyInt(),
                anyInt(),
                anyString(),
                anyBoolean(),
                anyFloat(),
                anyFloat(),
                anyFloat(),
                anyInt()
        )).thenReturn(branchListDTO);

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/branches?page=0&size=0&sorting_by=name&ascending=true&min_reservation_price=0&max_reservation_price=1000&min_score=0&min_capacity=0"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.branches", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReservationsPage() throws Exception {
        // Business business = utils.createBusiness(null);
        // Branch branch = utils.createBranch(business);
        // ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        // reservationDTOList.add(utils.createReservationDTO(null));
        // ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        // when(branchService.getReservations(anyLong())).thenReturn(reservationListDTO);
        // when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        // when(branchRepository.existsByIdAndBusinessId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reservations?reservation_date=2020-12-12&page=2&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }


    @Test
    public void shouldGetNoContentDueToMissingBranchInGetReservationsPage() throws Exception {
        
        when(branchService.getReservationsPage(anyLong(), any(Date.class), anyInt(), anyInt())).thenThrow(new NoContentException("Branch with id 1 does not exists", 20));

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reservations?reservation_date=2020-12-12&page=2&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Branch with id 1 does not exists")));
    }

    @Test
    public void shouldGetUnprocessableExceptionToPageLessThanZeroInGetReservationsPage() throws Exception{
        when(branchService.getReservationsPage(anyLong(), any(Date.class), anyInt(), anyInt())).thenThrow(new UnprocessableException("Page number cannot be less than zero", 40));

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reservations?reservation_date=2020-12-12&page=-1&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(40)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Page number cannot be less than zero")));
    }

    @Test
    public void shouldGetUnprocessableDueToPageSizeLessThanOneInGetReservationsPage() throws Exception{
        when(branchService.getReservationsPage(anyLong(), any(Date.class), anyInt(), anyInt())).thenThrow(new UnprocessableException("Size cannot be less than one", 41));

        utils.setAuthorities("business");

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reservations?reservation_date=2020-12-12&page=2&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(41)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Size cannot be less than one")));
    }

    @Test
    public void shouldGetReservationsPageByBranchIdInGetReservationsPage() throws Exception {

        ArrayList<ReservationDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dtoList.add(utils.createReservationDTO(null));
        }

        ReservationListDTO reviewListDTO =  ReservationListDTO.builder().reservations(dtoList).build();

        utils.setAuthorities("business");

        when(branchService.getReservationsPage(anyLong(), any(Date.class), anyInt(), anyInt())).thenReturn(reviewListDTO);

        mockMvc.perform(get(BranchStatics.Endpoint.PATH.concat("/1/reservations?reservation_date=2020-12-12&page=2&size=5"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations", CoreMatchers.hasItems()));
    }

}
