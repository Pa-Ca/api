package com.paca.paca.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.client.controller.ClientController;
import com.paca.paca.client.controller.ReviewController;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.dto.ProductListDTO;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { ClientController.class, ReviewController.class })
public class ProductControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private JwtService jwtService;

    @InjectMocks private ProductService productService;

    private TestUtils utils = TestUtils.builder().build();

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
}
