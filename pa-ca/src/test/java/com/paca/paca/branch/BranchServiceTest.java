package com.paca.paca.branch;

import org.junit.Assert;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import junit.framework.TestCase;

import com.paca.paca.ServiceTest;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.Review;
import com.paca.paca.sale.dto.TaxListDTO;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.TableListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.branch.dto.PaymentOptionListDTO;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BranchServiceTest extends ServiceTest {

    @InjectMocks
    private BranchService branchService;

    @Test 
    void shouldGetNoContentDueToMissingBranchInGetBranchById() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetBranchById() {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(branchMapper.toDTO(any(Branch.class))).thenReturn(dto);

        BranchDTO response = branchService.getById(branch.getId());

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInSaveBranch() {
        BranchDTO dto = utils.createBranchDTO(null);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business with id " + dto.getBusinessId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 21);
        }
    }

    @Test
    void shouldGetBranchDueToNonPositiveCapacityInSaveBranch() {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);
        dto.setCapacity((short) 0);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch.getBusiness()));

        try {
            branchService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "Branch capacity must be greater than 0");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 22);
        }
    }

    @Test
    void shouldSaveBranch() {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch.getBusiness()));
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);
        when(branchMapper.toEntity(any(BranchDTO.class), any(Business.class))).thenReturn(branch);
        when(branchMapper.toDTO(any(Branch.class))).thenReturn(dto);

        BranchDTO response = branchService.save(dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInUpdateBranch() {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.update(branch.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branch.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetBadRequestDueToNonPositiveCapacityInUpdateBranch() {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);
        dto.setCapacity((short) 0);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));

        try {
            branchService.update(branch.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "Branch capacity must be greater than 0");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 21);
        }
    }

    @Test
    void shouldUpdateBranch() {
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);
        when(branchMapper.updateModel(any(BranchDTO.class), any(Branch.class))).thenReturn(branch);
        when(branchMapper.toDTO(any(Branch.class))).thenReturn(dto);

        BranchDTO response = branchService.update(branch.getId(), dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInDeleteBranch() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.delete(branch.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branch.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInProductSubCategories() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getProductSubCategories(branch.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branch.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetProductSubCategories() {
        Branch branch = utils.createBranch(null);
        List<ProductSubCategory> subCategories = TestUtils.castList(
                ProductSubCategory.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(productSubCategoryRepository.findAllByBranchId(any(Long.class))).thenReturn(subCategories);

        ProductSubCategoryListDTO dtoResponse = branchService.getProductSubCategories(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingBranchInGetProducts() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getProducts(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetProducts() {
        Branch branch = utils.createBranch(null);
        List<Product> products = TestUtils.castList(
                Product.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(productRepository.findAllBySubCategory_Branch_Id(any(Long.class))).thenReturn(products);

        ProductListDTO dtoResponse = branchService.getProducts(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingBranchInGetPromotions() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getPromotions(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetPromotions() {
        Branch branch = utils.createBranch(null);
        List<Promotion> promotions = TestUtils.castList(
                Promotion.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(promotionRepository.findAllByBranchId(any(Long.class))).thenReturn(promotions);

        PromotionListDTO dtoResponse = branchService.getPromotions(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingBranchInGetFavoriteClients() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getFavoriteClients(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetFavoriteClients() {
        Branch branch = utils.createBranch(null);
        List<FavoriteBranch> favs = TestUtils.castList(
                FavoriteBranch.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(favoriteBranchRepository.findAllByClientId(any(Long.class))).thenReturn(favs);

        ClientListDTO dtoResponse = branchService.getFavoriteClients(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test    
    void shouldGetNoContentDueToMissingBranchInGetReviewsPage() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Long branchId = 1L;
        try {
            branchService.getReviewsPage(branchId, 0, 10);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branchId + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetUnprocessableDueToPageLessThanZeroInGetReviewsPage() {
        Branch branch = utils.createBranch(null);
        Long branchId = branch.getId();

        try {
            branchService.getReviewsPage(branchId, -1, 10);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page number cannot be less than zero");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 44);
        }
    }

    @Test
    void shouldGetUnprocessableDueToSizeLessThanOneInGetReviewsPage() {
        Branch branch = utils.createBranch(null);
        Long branchId = branch.getId();

        try {
            branchService.getReviewsPage(branchId, 1, 0);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page size cannot be less than one");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 45);
        }
    }

    @Test
    void shouldGetPageInReviewsPage() {
        Branch branch = utils.createBranch(null);
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        Long branchId = branch.getId();

        Pageable pageable = Mockito.mock(Pageable.class);
        when(pageable.getPageSize()).thenReturn(10);

        // Create 20 reviews manually
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            reviews.add(utils.createReview(null, null));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviews.size());
        Page<Review> pagedResult = new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());

        when(reviewRepository.findAllByBranchId(any(Long.class), any(Pageable.class))).thenReturn(pagedResult);
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(utils.createReviewDTO(null));

        ReviewListDTO pageResponse = branchService.getReviewsPage(branchId, 0, 10);
        ReviewListDTO pageResponse2 = branchService.getReviewsPage(branchId, 1, 10);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse2).isNotNull();

        assertThat(pageResponse.getReviews().size()).isEqualTo(10);
        assertThat(pageResponse2.getReviews().size()).isEqualTo(10);
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInGetDefaultTaxesByBranchId() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getDefaultTaxesByBranchId(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetDefaultTaxesByBranchId() {
        Branch branch = utils.createBranch(null);
        List<Tax> taxes = TestUtils.castList(
                Tax.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(taxRepository.findAllByBranchId(any(Long.class))).thenReturn(taxes);

        TaxListDTO dtoResponse = branchService.getDefaultTaxesByBranchId(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInGetTablesBrBranchId() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getTablesByBranchId(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetTablesByBranchId() {
        Branch branch = utils.createBranch(null);
        List<Table> tables = TestUtils.castList(
                Table.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(tableRepository.findAllByBranchId(any(Long.class))).thenReturn(tables);

        TableListDTO dtoResponse = branchService.getTablesByBranchId(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInGetPaymentOptionsByBranchId() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getPaymentOptionsByBranchId(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetPaymentOptionsByBranchId() {
        Branch branch = utils.createBranch(null);
        List<PaymentOption> options = TestUtils.castList(
                PaymentOption.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(paymentOptionRepository.findAllByBranchId(any(Long.class))).thenReturn(options);

        PaymentOptionListDTO dtoResponse = branchService.getPaymentOptionsByBranchId(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

}
