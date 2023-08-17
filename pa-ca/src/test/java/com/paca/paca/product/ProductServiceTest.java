package com.paca.paca.product;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.product.service.ProductService;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductSubCategoryRepository productSubCategoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private TestUtils utils = TestUtils.builder().build();

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

        ProductDTO response = productService.getById(product.getId());

        assertThat(response).isEqualTo(dto);
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
    void shouldGetConflictDueToNameRepeatedInSave() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productSubCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(product.getSubCategory()));
        when(productRepository.existsBySubCategoryIdAndName(any(Long.class), any(String.class))).thenReturn(true);

        try {
            productService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Product with name " + dto.getName() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 57);
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

        ProductDTO response = productService.save(dto);

        assertThat(response).isEqualTo(dto);
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
    void shouldGetConflictDueToNameRepeatedInUpdate() {
        Product product = utils.createProduct(null);
        ProductDTO dto = utils.createProductDTO(product);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
        when(productRepository.existsBySubCategoryIdAndName(any(Long.class), any(String.class))).thenReturn(true);

        try {
            productService.update(product.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Product with name " + dto.getName() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 57);
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

        ProductDTO response = productService.update(product.getId(), dto);

        assertThat(response).isEqualTo(dto);
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
        ProductDTO response = productService.getById(product.getId());

        assertThat(response).isNull();
    }
}
