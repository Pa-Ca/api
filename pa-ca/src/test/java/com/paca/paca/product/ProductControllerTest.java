package com.paca.paca.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.business.model.Business;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.statics.ClientStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.product.controller.ProductController;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.service.ProductService;
import com.paca.paca.product.statics.ProductStatics;
import com.paca.paca.utils.TestUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { ProductController.class })
public class ProductControllerTest extends ControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private JwtService jwtService;

    @MockBean private ProductService productService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetProductList() throws Exception {
        ArrayList<ProductDTO> productsDTOList = new ArrayList<>();
        productsDTOList.add(utils.createProductDTO(null));
        ProductListDTO productListDTO = ProductListDTO.builder().products(productsDTOList).build();

        when(productService.getAll()).thenReturn(productListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                    CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                    CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetProductList() throws Exception {
        ArrayList<ProductDTO> productsDTOList = new ArrayList<>();
        productsDTOList.add(utils.createProductDTO(null));
        ProductListDTO productListDTO = ProductListDTO.builder().products(productsDTOList).build();

        when(productService.getAll()).thenReturn(productListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveProduct() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productService.save(any(ProductDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(post(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                    CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSaveProduct() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productService.save(any(ProductDTO.class))).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSaveProduct() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productService.save(any(ProductDTO.class))).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveProduct() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productService.save(any(ProductDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(ProductStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subCategoryId", CoreMatchers.is(dto.getSubCategoryId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.disabled", CoreMatchers.is(dto.getDisabled())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(dto.getPrice().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(dto.getDescription())));
    }

    @Test
    public void shouldGetNoContentInGetProductById() throws Exception {
        when(productService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(get(ProductStatics.Endpoint.PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetProductById() throws Exception {
        ProductDTO dto = utils.createProductDTO(null);

        when(productService.getById(anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(get(ProductStatics.Endpoint.PATH.concat("/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subCategoryId", CoreMatchers.is(dto.getSubCategoryId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.disabled", CoreMatchers.is(dto.getDisabled())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(dto.getPrice().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(dto.getDescription())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdateProductById() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        utils.setAuthorities("client");

        mockMvc.perform(put(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInUpdateProductById() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);
        Business business = utils.createBusiness(null);

        when(productService.update(anyLong(), any(ProductDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(put(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdateProductById() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);
        Business business = utils.createBusiness(null);

        when(productService.update(anyLong(), any(ProductDTO.class))).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateProductById() throws Exception {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);
        Business business = utils.createBusiness(null);

        when(productService.update(anyLong(), any(ProductDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subCategoryId", CoreMatchers.is(dto.getSubCategoryId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.disabled", CoreMatchers.is(dto.getDisabled())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(dto.getPrice().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(dto.getDescription())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteProductById() throws Exception {
        Product product = utils.createProduct(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(delete(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteProductById() throws Exception {
        Product product = utils.createProduct(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("business");

        mockMvc.perform(delete(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteProductById() throws Exception {
        Product product = utils.createProduct(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        doThrow(new NoContentException("message", 0)).when(productService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteProductById() throws Exception {
        Product product = utils.createProduct(null);
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        when(productRepository.existsByIdAndSubCategory_Branch_Business_Id(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(productService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(ProductStatics.Endpoint.PATH.concat("/" + product.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
