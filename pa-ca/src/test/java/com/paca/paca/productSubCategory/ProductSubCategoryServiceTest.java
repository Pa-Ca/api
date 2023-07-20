package com.paca.paca.productSubCategory;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import junit.framework.TestCase;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.dto.ProductCategoryListDTO;
import com.paca.paca.productSubCategory.utils.ProductCategoryMapper;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.productSubCategory.utils.ProductSubCategoryMapper;
import com.paca.paca.productSubCategory.service.ProductSubCategoryService;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductSubCategoryServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductSubCategoryRepository productSubCategoryRepository;

    @Mock
    private ProductCategoryMapper productCategoryMapper;

    @Mock
    private ProductSubCategoryMapper productSubCategoryMapper;

    @InjectMocks
    private ProductSubCategoryService productSubCategoryService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldGetAllProductSubCategories() {
        List<ProductSubCategory> productSubCategories = TestUtils.castList(
                ProductSubCategory.class,
                Mockito.mock(List.class));

        when(productSubCategoryRepository.findAll()).thenReturn(productSubCategories);
        ProductSubCategoryListDTO responseDTO = productSubCategoryService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetAllProductCategories() {
        List<ProductCategory> productCategories = TestUtils.castList(
                ProductCategory.class,
                Mockito.mock(List.class));

        when(productCategoryRepository.findAll()).thenReturn(productCategories);
        ProductCategoryListDTO responseDTO = productSubCategoryService.getAllProductCategories();

        assertThat(responseDTO).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingProductSubCategoryInGetProductSubCategoryById() {
        when(productSubCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productSubCategoryService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Product sub-category with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 23);
        }
    }

    @Test
    void shouldGetProductSubCategoryById() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

        when(productSubCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(productSubCategory));
        when(productSubCategoryMapper.toDTO(any(ProductSubCategory.class))).thenReturn(dto);

        ProductSubCategoryDTO dtoResponse = productSubCategoryService.getById(productSubCategory.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(productSubCategory.getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(productSubCategory.getBranch().getId());
        assertThat(dtoResponse.getCategoryId()).isEqualTo(productSubCategory.getCategory().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSaveProductSubCategory() {
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productSubCategoryService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + dto.getBranchId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingProductCategoryInSaveProductSubCategory() {
        Branch branch = utils.createBranch(null);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(productCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productSubCategoryService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Product category with id " + dto.getCategoryId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 24);
        }
    }

    @Test
    void shouldGetConflictDueToNameRepeatedInSaveProductSubCategory() {
        Branch branch = utils.createBranch(null);
        ProductCategory productCategory = utils.createProductCategory();
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(productCategoryRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(productCategory));
        when(productSubCategoryRepository.existsByBranchIdAndCategoryIdAndName(anyLong(), anyLong(), any(String.class)))
                .thenReturn(true);

        try {
            productSubCategoryService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Product sub-category with name " + dto.getName() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 56);
        }
    }

    @Test
    void shouldSaveProductSubCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

        when(branchRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(productSubCategory.getBranch()));
        when(productCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(productSubCategory.getCategory()));
        when(productSubCategoryRepository.save(any(ProductSubCategory.class))).thenReturn(productSubCategory);
        when(productSubCategoryMapper.toEntity(
                any(ProductSubCategoryDTO.class),
                any(Branch.class),
                any(ProductCategory.class)))
                .thenReturn(productSubCategory);
        when(productSubCategoryMapper.toDTO(any(ProductSubCategory.class))).thenReturn(dto);
        when(productSubCategoryRepository.existsByBranchIdAndCategoryIdAndName(anyLong(), anyLong(), any(String.class)))
                .thenReturn(false);

        ProductSubCategoryDTO dtoResponse = productSubCategoryService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(productSubCategory.getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(productSubCategory.getBranch().getId());
        assertThat(dtoResponse.getCategoryId()).isEqualTo(productSubCategory.getCategory().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingProductSubCategoryInUpdateProductSubCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

        when(productSubCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productSubCategoryService.update(productSubCategory.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(),
                    "Product sub-category with id " + productSubCategory.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 23);
        }
    }

    @Test
    void shouldGetConflictDueToNameRepeatedInUpdateSubCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

        when(productSubCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(productSubCategory));
        when(productSubCategoryRepository.existsByBranchIdAndCategoryIdAndName(anyLong(), anyLong(), any(String.class)))
                .thenReturn(true);

        try {
            productSubCategoryService.update(productSubCategory.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Product sub-category with name " + dto.getName() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 56);
        }
    }

    @Test
    void shouldUpdateProductSubCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

        when(productSubCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(productSubCategory));
        when(productSubCategoryRepository.save(any(ProductSubCategory.class))).thenReturn(productSubCategory);
        when(productSubCategoryMapper.updateModel(any(ProductSubCategoryDTO.class), any(ProductSubCategory.class)))
                .thenReturn(productSubCategory);
        when(productSubCategoryMapper.toDTO(any(ProductSubCategory.class))).thenReturn(dto);
        when(productSubCategoryRepository.existsByBranchIdAndCategoryIdAndName(anyLong(), anyLong(), any(String.class)))
                .thenReturn(false);

        ProductSubCategoryDTO dtoResponse = productSubCategoryService.update(productSubCategory.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(productSubCategory.getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(productSubCategory.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingProductSubCategoryInDeleteProductSubCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        when(productSubCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            productSubCategoryService.delete(productSubCategory.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(),
                    "Product sub-category with id " + productSubCategory.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 23);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingProductSubCategoryInGetAllProductsByProductCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        when(productSubCategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            productSubCategoryService.getAllProducts(productSubCategory.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(),
                    "Product sub-category with id " + productSubCategory.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 23);
        }
    }

    @Test
    void shouldGetAllProductsByProductCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        List<Product> products = TestUtils.castList(Product.class, Mockito.mock(List.class));

        when(productSubCategoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(productSubCategory));
        when(productRepository.findAllBySubCategoryId(anyLong())).thenReturn(products);

        ProductListDTO responseDTO = productSubCategoryService.getAllProducts(productSubCategory.getId());

        assertThat(responseDTO).isNotNull();
    }

}
