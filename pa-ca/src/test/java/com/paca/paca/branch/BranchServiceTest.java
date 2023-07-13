package com.paca.paca.branch;

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
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.client.utils.ReviewMapper;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.branch.utils.AmenityMapper;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BranchAmenityRepository branchAmenityRepository;

    @Mock
    private FavoriteBranchRepository favoriteBranchRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductSubCategoryRepository productSubCategoryRepository;

    @Mock
    private BranchMapper branchMapper;

    @Mock
    private AmenityMapper amenityMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private BranchService branchService;

    @InjectMocks
    private AmenityService amenityService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldGetAllBranches() {
        List<Branch> branches = TestUtils.castList(Branch.class, Mockito.mock(List.class));

        when(branchRepository.findAll()).thenReturn(branches);
        BranchListDTO responseDTO = branchService.getAll();

        assertThat(responseDTO).isNotNull();
    }

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

        BranchDTO dtoResponse = branchService.getById(branch.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(branch.getId());
        assertThat(dtoResponse.getBusinessId()).isEqualTo(branch.getBusiness().getId());
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
        dto.setCapacity(0);

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

        BranchDTO dtoResponse = branchService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(branch.getId());
        assertThat(dtoResponse.getBusinessId()).isEqualTo(branch.getBusiness().getId());
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
        dto.setCapacity(0);

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

        BranchDTO dtoResponse = branchService.update(branch.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(branch.getId());
        assertThat(dtoResponse.getBusinessId()).isEqualTo(branch.getBusiness().getId());
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
    void shouldGetNoContentDueToMissingBranchInGetReservations() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getReservations(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetReservations() {
        Branch branch = utils.createBranch(null);
        List<Reservation> reservations = TestUtils.castList(
                Reservation.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(reservationRepository.findAllByBranchId(any(Long.class))).thenReturn(reservations);

        ReservationListDTO dtoResponse = branchService.getReservations(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingBranchInGetReservationsByDate() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getReservations(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetReservationsByDate() {
        Branch branch = utils.createBranch(null);
        List<Reservation> reservations = TestUtils.castList(
                Reservation.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(reservationRepository.findAllByBranchIdAndReservationDateInGreaterThanEqual(
                any(Long.class),
                any(Date.class))).thenReturn(reservations);

        ReservationListDTO dtoResponse = branchService.getReservationsByDate(
                branch.getId(),
                new Date(System.currentTimeMillis()));

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
    void shouldGetNoContentDueToMissingBranchInGetReviews() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            branchService.getReviews(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetReviews() {
        Branch branch = utils.createBranch(null);
        List<Review> reviews = TestUtils.castList(
                Review.class,
                Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(reviewRepository.findAllByBranchId(any(Long.class))).thenReturn(reviews);

        ReviewListDTO dtoResponse = branchService.getReviews(branch.getId());

        assertThat(dtoResponse).isNotNull();
    }

    @Test
    void shouldGetAllAmenities() {
        List<Amenity> amenities = TestUtils.castList(Amenity.class, Mockito.mock(List.class));

        when(amenityRepository.findAll()).thenReturn(amenities);
        AmenityListDTO responseDTO = amenityService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetAllAmenitiesBySearchWord() {
        List<Amenity> amenities = TestUtils.castList(Amenity.class, Mockito.mock(List.class));

        when(amenityRepository.findAllMatching(any(String.class))).thenReturn(amenities);
        AmenityListDTO responseDTO = amenityService.getBySearchWord("test");

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInAllAmenitiesByBranchId() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.getAllByBranchId(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        List<BranchAmenity> branchAmenities = TestUtils.castList(BranchAmenity.class, Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(branchAmenityRepository.findAllByBranchId(any(Long.class))).thenReturn(branchAmenities);

        AmenityListDTO responseDTO = amenityService.getAllByBranchId(branch.getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSaveAllAmenitiesByBranchId() {
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.saveAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingAmenityInSaveAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class))).thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.saveAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Amenity does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldSaveAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        BranchAmenity branchAmenity = utils.createBranchAmenity(branch, amenity);
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();
        List<BranchAmenity> branchAmenities = TestUtils.castList(BranchAmenity.class, Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class)))
                .thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(amenity));
        when(branchAmenityRepository.findByBranchIdAndAmenityId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());
        when(branchAmenityRepository.save(any(BranchAmenity.class)))
                .thenReturn(branchAmenity);
        when(branchAmenityRepository.findAllByBranchId(any(Long.class)))
                .thenReturn(branchAmenities);

        AmenityListDTO responseDTO = amenityService.saveAllByBranchId(branch.getId(), amenitiesDTO);

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInDeleteAllAmenitiesByBranchId() {
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.deleteAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingAmenityInDeleteAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class))).thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.deleteAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Amenity does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldDeleteAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        BranchAmenity branchAmenity = utils.createBranchAmenity(branch, amenity);
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();
        List<BranchAmenity> branchAmenities = TestUtils.castList(BranchAmenity.class, Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class)))
                .thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(amenity));
        when(branchAmenityRepository.findByBranchIdAndAmenityId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.ofNullable(branchAmenity));
        when(branchAmenityRepository.findAllByBranchId(any(Long.class)))
                .thenReturn(branchAmenities);

        AmenityListDTO responseDTO = amenityService.deleteAllByBranchId(branch.getId(), amenitiesDTO);

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingAmenityInGetAllBranchesByAmenityId() {
        when(amenityRepository.existsById(any(Long.class))).thenReturn(false);

        try {
            amenityService.getAllBranches(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Amenity with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 34);
        }
    }

    @Test
    void shouldGetAllBranchesByAmenityId() {
        Amenity amenity = utils.createAmenity();
        List<BranchAmenity> branchAmenities = TestUtils.castList(
                BranchAmenity.class,
                Mockito.mock(List.class));

        when(amenityRepository.existsById(any(Long.class))).thenReturn(true);
        when(branchAmenityRepository.findAllByAmenityId(any(Long.class))).thenReturn(branchAmenities);

        BranchListDTO responseDTO = amenityService.getAllBranches(amenity.getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    // We are going to test test the exception when the requested page is from a non existing branch
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

    // Test for getPage method
    // Lets test the exception when the page is less than 0
    @Test
    void shouldGetUnprocessableDueToPageLessThanZeroInGetReviewsPage() {
        // Create a branch
        Branch branch = utils.createBranch(null);
        // Mock the branch repository
        // when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        // Get that branch id
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

    // Lets test the exception when the size is less than 1
    @Test
    void shouldGetUnprocessableDueToSizeLessThanOneInGetReviewsPage() {
        // Create a branch
        Branch branch = utils.createBranch(null);
        // Mock the branch repository
        // when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        // Get that branch id
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

    // Now lets test the that the getPage method works as expected
    // First lets create 20 reviews
    // Then lets get the first page with 10 reviews
    // Then lets get the second page with 10 reviews
    // Check if the first page has 10 reviews
    // Check if the second page has 10 reviews
    @Test
    void shouldGetPageInReviewsPage() {

        // Create a branch
        Branch branch = utils.createBranch(null);
        // Mock the branch repository
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        // Get that branch id
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
    void shouldGetUnprocessableExceptionDueToPageLessThanZeroInGetBranchesPage() {
        // Create a branch
        // Branch branch = utils.createBranch(null);
        // Mock the branch repository
        // when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        // Get that branch id
        // Long branchId = branch.getId();

        try {
            branchService.getBranchesPage(
                    -1,
                    10,
                    "name",
                    true,
                    null,
                    null,
                    null,
                    0);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page number cannot be less than zero");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 44);
        }
    }

    @Test
    void shouldGetUnprocessableDueToSizeLessThanOneInGetBranchesPage() {
        // Create a branch
        utils.createBranch(null);

        try {
            branchService.getBranchesPage(
                    0,
                    0,
                    "name",
                    true,
                    null,
                    null,
                    null,
                    0);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page size cannot be less than one");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 45);
        }
    }

    @Test
    void shouldGetUnprocessableDueToInvalidSortingKeyInGetBranchesPage() {
        utils.createBranch(null);

        try {
            branchService.getBranchesPage(
                    10,
                    10,
                    "invalid_key_string",
                    true,
                    null,
                    null,
                    null,
                    0);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Sorting key is not valid");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 46);
        }
    }

    @Test
    void shouldGetPageInGetBranchesPage() {

        List<Branch> branches = utils.createTestBranches(null);

        when(branchRepository.findAllByReservationPriceBetweenAndScoreGreaterThanEqualAndCapacityGreaterThanEqual(
                any(BigDecimal.class),
                any(BigDecimal.class),
                any(Float.class),
                any(Integer.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(branches));

        BranchListDTO pageResponse = branchService.getBranchesPage(
                1,
                5, // This parameter doesn't affect the test
                "name",
                true,
                BigDecimal.valueOf(0.0f),
                BigDecimal.valueOf(5.0f),
                0.0f,
                0);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getBranches().size()).isEqualTo(branches.size());

    }

    @Test
    // We are going to test test the exception when the requested page is from a non existing branch
    void shouldGetNoContentDueToMissingBranchInGetReservationsPage() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Date date = new Date();
        Long branchId = 1L;
        try {
            branchService.getReservationsPage(branchId, date, 0, 10);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branchId + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    // Test for getPage method
    // Lets test the exception when the page is less than 0
    @Test
    void shouldGetUnprocessableDueToPageLessThanZeroInGetReservationsPage() {
        // Create a branch
        Branch branch = utils.createBranch(null);
        Long branchId = branch.getId();
        Date date = new Date();

        try {
            branchService.getReservationsPage(branchId, date, -1, 10);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page number cannot be less than zero");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 44);
        }
    }

    // Lets test the exception when the size is less than 1
    @Test
    void shouldGetUnprocessableDueToSizeLessThanOneInGetReservationsPage() {
        // Create a branch
        Branch branch = utils.createBranch(null);
        Long branchId = branch.getId();
        Date date = new Date();

        try {
            branchService.getReservationsPage(branchId, date, 1, 0);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page size cannot be less than one");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 45);
        }
    }

    // Now lets test the that the getPageReservationsPAge method works as expected
    // First lets create 20 reservations
    // Then lets get the first page with 10 reservations
    // Then lets get the second page with 10 reservations
    // Check if the first page has 10 reservations
    // Check if the second page has 10 reservations
    @Test
    void shouldGetPageInGetReservationsPage() {
        // Create a branch
        Branch branch = utils.createBranch(null);
        // Mock the branch repository
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        // Get that branch id
        Long branchId = branch.getId();
        Pageable pageable = Mockito.mock(Pageable.class);
        when(pageable.getPageSize()).thenReturn(10);
        // Create 20 reservations manually
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            reservations.add(utils.createReservation(null, null));
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reservations.size());
        Page<Reservation> pagedResult = new PageImpl<>(reservations.subList(start, end), pageable, reservations.size());

        when(reservationRepository.findAllByBranchIdAndReservationDateInGreaterThanEqual(any(Long.class), any(Date.class),
                any(Pageable.class))).thenReturn(pagedResult);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(utils.createReservationDTO(null));

        Date date = new Date();
        ReservationListDTO pageResponse = branchService.getReservationsPage(branchId, date, 0, 10);
        ReservationListDTO pageResponse2 = branchService.getReservationsPage(branchId, date, 1, 10);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse2).isNotNull();
        assertThat(pageResponse.getReservations().size()).isEqualTo(10);
        assertThat(pageResponse2.getReservations().size()).isEqualTo(10);
    }

}
