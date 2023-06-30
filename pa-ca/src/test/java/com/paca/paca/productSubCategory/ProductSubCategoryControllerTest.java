package com.paca.paca.productSubCategory;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.product.dto.ProductListDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.productSubCategory.dto.ProductCategoryDTO;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.dto.ProductCategoryListDTO;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.productSubCategory.service.ProductSubCategoryService;
import com.paca.paca.productSubCategory.statics.ProductSubCategoryStatics;
import com.paca.paca.productSubCategory.controller.ProductSubCategoryController;

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
@WebMvcTest(controllers = { ProductSubCategoryController.class })
public class ProductSubCategoryControllerTest extends ControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private ProductSubCategoryService productSubCategoryService;

        private TestUtils utils = TestUtils.builder().build();

        @Test
        public void shouldGetForbiddenDueToInvalidRoleInGetProductSubCategoryList() throws Exception {
                ArrayList<ProductSubCategoryDTO> dtoList = new ArrayList<>();
                dtoList.add(utils.createProductSubCategoryDTO(null));
                ProductSubCategoryListDTO productSubCategoryListDTO = ProductSubCategoryListDTO.builder()
                                .productSubCategories(dtoList).build();

                when(productSubCategoryService.getAll()).thenReturn(productSubCategoryListDTO);

                utils.setAuthorities("client");
                mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));

                utils.setAuthorities("business");
                mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        public void shouldGetProductSubCategoryList() throws Exception {
                ArrayList<ProductSubCategoryDTO> dtoList = new ArrayList<>();
                dtoList.add(utils.createProductSubCategoryDTO(null));
                ProductSubCategoryListDTO productSubCategoryListDTO = ProductSubCategoryListDTO.builder()
                                .productSubCategories(dtoList).build();

                when(productSubCategoryService.getAll()).thenReturn(productSubCategoryListDTO);

                utils.setAuthorities("admin");

                mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productSubCategories",
                                                CoreMatchers.hasItems()));
        }

        @Test
        public void shouldGetProductCategoryList() throws Exception {
                ArrayList<ProductCategoryDTO> dtoList = new ArrayList<>();
                dtoList.add(utils.createProductCategoryDTO(null));
                ProductCategoryListDTO productCategoryListDTO = ProductCategoryListDTO.builder()
                                .productCategories(dtoList).build();

                when(productSubCategoryService.getAllProductCategories()).thenReturn(productCategoryListDTO);

                utils.setAuthorities("admin");

                mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH.concat("/categories"))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategories",
                                                CoreMatchers.hasItems()));
        }

        @Test
        public void shouldGetForbiddenDueToInvalidRoleInSaveProductSubCategory() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

                when(productSubCategoryService.save(any(ProductSubCategoryDTO.class))).thenReturn(dto);

                utils.setAuthorities("client");

                mockMvc.perform(post(ProductSubCategoryStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        public void shouldGetNoContentInSaveProductSubCategory() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

                when(productSubCategoryService.save(any(ProductSubCategoryDTO.class)))
                                .thenThrow(new NoContentException("message", 0));

                utils.setAuthorities("business");

                mockMvc.perform(post(ProductSubCategoryStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
        }

        @Test
        public void shouldSaveProductSubCategory() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

                when(productSubCategoryService.save(any(ProductSubCategoryDTO.class))).thenReturn(dto);

                utils.setAuthorities("business");

                mockMvc.perform(post(ProductSubCategoryStatics.Endpoint.PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                CoreMatchers.is(dto.getId().intValue())))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.branchId",
                                                                CoreMatchers.is(dto.getBranchId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",
                                                CoreMatchers.is(dto.getCategoryId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())));
        }

    @Test
    public void shouldGetNoContentInGetProductSubCategoryById() throws Exception {
        when(productSubCategoryService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

        @Test
        public void shouldGetProductSubCategoryById() throws Exception {
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(null);

                when(productSubCategoryService.getById(anyLong())).thenReturn(dto);

                utils.setAuthorities("business");

                mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + dto.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                CoreMatchers.is(dto.getId().intValue())))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.branchId",
                                                                CoreMatchers.is(dto.getBranchId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",
                                                CoreMatchers.is(dto.getCategoryId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())));
        }

        @Test
        public void shouldGetForbiddenDueToInvalidRoleInUpdateProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

                utils.setAuthorities("client");

                mockMvc.perform(put(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        public void shouldGetForbiddenDueToInvalidUserInUpdateProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);
                Business business = utils.createBusiness(null);

                when(productSubCategoryService.update(anyLong(), any(ProductSubCategoryDTO.class))).thenReturn(dto);
                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(false);

                utils.setAuthorities("business");

                mockMvc.perform(put(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        public void shouldGetNoContentInUpdateProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                Business business = utils.createBusiness(null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

                when(productSubCategoryService.update(anyLong(), any(ProductSubCategoryDTO.class)))
                                .thenThrow(new NoContentException("message", 0));
                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);

                utils.setAuthorities("business");

                mockMvc.perform(put(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
        }

        @Test
        public void shouldUpdateProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                Business business = utils.createBusiness(null);
                ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

                when(productSubCategoryService.update(anyLong(), any(ProductSubCategoryDTO.class))).thenReturn(dto);
                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);

                utils.setAuthorities("business");

                mockMvc.perform(put(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                CoreMatchers.is(dto.getId().intValue())))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.branchId",
                                                                CoreMatchers.is(dto.getBranchId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId",
                                                CoreMatchers.is(dto.getCategoryId().intValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())));
        }

        @Test
        public void shouldGetForbiddenDueToInvalidRoleInDeleteProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                Business business = utils.createBusiness(null);

                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);

                utils.setAuthorities("client");

                mockMvc.perform(delete(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        public void shouldGetForbiddenDueToInvalidUserInDeleteProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                Business business = utils.createBusiness(null);

                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(false);

                utils.setAuthorities("business");

                mockMvc.perform(delete(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isForbidden())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                CoreMatchers.is("Unauthorized access for this operation")));
        }

        @Test
        public void shouldGetNoContentInDeleteProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                Business business = utils.createBusiness(null);

                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);
                doThrow(new NoContentException("message", 0)).when(productSubCategoryService).delete(anyLong());

                utils.setAuthorities("business");

                mockMvc.perform(delete(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
        }

        @Test
        public void shouldDeleteProductSubCategoryById() throws Exception {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                Business business = utils.createBusiness(null);

                when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
                when(productSubCategoryRepository.existsByIdAndBranch_Business_Id(anyLong(), anyLong()))
                                .thenReturn(true);
                doNothing().when(productSubCategoryService).delete(anyLong());

                utils.setAuthorities("business");

                mockMvc.perform(delete(ProductSubCategoryStatics.Endpoint.PATH.concat("/" + productSubCategory.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

    @Test
    public void shouldGetNoContentDueToInvalidRoleInGetAllProductsByProductSubCategoryId() throws Exception {
        when(productSubCategoryService.getAllProducts(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH.concat("/1/product"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

        @Test
        public void shouldGetAllProductsByProductSubCategoryId() throws Exception {
                ArrayList<ProductDTO> dtoList = new ArrayList<>();
                dtoList.add(utils.createProductDTO(null));
                ProductListDTO productListDTO = ProductListDTO.builder()
                                .products(dtoList).build();

                when(productSubCategoryService.getAllProducts(anyLong())).thenReturn(productListDTO);

                utils.setAuthorities("client");

                mockMvc.perform(get(ProductSubCategoryStatics.Endpoint.PATH.concat("/1/product"))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.products", CoreMatchers.hasItems()));
        }

}
