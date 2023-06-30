package com.paca.paca.product;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.product.service.ProductService;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductSubCategoryRepository productSubCategoryRepository;
    @Mock
    private ProductCategoryRepository productCategoryRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldGetAllClients() {
        List<Product> products = TestUtils.castList(Product.class, Mockito.mock(List.class));

        when(productRepository.findAll()).thenReturn(products);
        ProductListDTO responseDTO = productService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingProductInGetProductById() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Product with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 25);
        }
    }

    @Test
    void shouldGetProductById() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
        when(productMapper.toDTO(any(Product.class))).thenReturn(dto);

        ProductDTO dtoResponse = productService.getById(product.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(product.getId());
        assertThat(dtoResponse.getSubCategoryId()).isEqualTo(product.getSubCategory().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingProductSubCategoryInSave() {
        ProductDTO dto = utils.createProductDTO(null);

        when(productSubCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(),
                    "Product sub-category with id " + dto.getSubCategoryId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 23);
        }
    }

    @Test
    void shouldSaveProduct() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productSubCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(product.getSubCategory()));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toEntity(any(ProductDTO.class), any(ProductSubCategory.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(dto);

        ProductDTO dtoResponse = productService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(product.getId());
        assertThat(dtoResponse.getName()).isEqualTo(product.getName());
        assertThat(dtoResponse.getPrice()).isEqualTo(product.getPrice());
        assertThat(dtoResponse.getDisabled()).isEqualTo(product.getDisabled());
        assertThat(dtoResponse.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void shouldGetNoContentDueToMissingProductInUpdate() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productService.update(product.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Product with id: " + dto.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 25);
        }
    }

    @Test
    void shouldUpdateProduct() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.updateModel(any(ProductDTO.class), any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(dto);

        ProductDTO dtoResponse = productService.update(product.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(product.getId());
        assertThat(dtoResponse.getName()).isEqualTo(product.getName());
        assertThat(dtoResponse.getPrice()).isEqualTo(product.getPrice());
        assertThat(dtoResponse.getDisabled()).isEqualTo(product.getDisabled());
        assertThat(dtoResponse.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void shouldGetNoContentDueToMissingProductInDelete() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productService.delete(product.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Product with id: " + dto.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 25);
        }
    }

    @Test
    void shouldDeleteProduct() {
        Product product = utils.createProduct(null);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));

        productService.delete(product.getId());
        ProductDTO dtoResponse = productService.getById(product.getId());

        assertThat(dtoResponse).isNull();
    }
}
