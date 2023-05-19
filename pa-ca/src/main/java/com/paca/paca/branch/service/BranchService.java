package com.paca.paca.branch.service;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.business.model.Business;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.ReviewMapper;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.promotion.utils.PromotionMapper;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.reservation.utils.ReservationMapper;

import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.product_sub_category.utils.ProductSubCategoryMapper;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import com.paca.paca.client.model.Review;
import com.paca.paca.client.repository.ReviewLikeRepository;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchMapper branchMapper;

    private final ReviewMapper reviewMapper;

    private final ProductMapper productMapper;

    private final PromotionMapper promotionMapper;

    private final ReservationMapper reservationMapper;

    private final ProductSubCategoryMapper productSubCategoryMapper;

    private final ClientMapper clientMapper;

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final GuestRepository guestRepository;

    private final ClientRepository clientRepository;

    private final BranchRepository branchRepository;

    private final ProductRepository productRepository;

    private final BusinessRepository businessRepository;

    private final PromotionRepository promotionRepository;

    private final ReservationRepository reservationRepository;

    private final ClientGroupRepository clientGroupRepository;

    private final FavoriteBranchRepository favoriteBranchRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductSubCategoryRepository productSubCategoryRepository;

    public BranchListDTO getAll() {
        List<BranchDTO> response = new ArrayList<>();
        branchRepository.findAll().forEach(branch -> {
            BranchDTO dto = branchMapper.toDTO(branch);
            response.add(dto);
        });

        return BranchListDTO.builder().branches(response).build();
    }

    public BranchDTO getById(Long id) throws NoContentException {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Branch with id " + id + " does not exists",
                        20));

        BranchDTO dto = branchMapper.toDTO(branch);
        return dto;
    }

    public BranchDTO save(BranchDTO dto) throws NoContentException, BadRequestException {
        Optional<Business> business = businessRepository.findById(dto.getBusinessId());
        if (business.isEmpty()) {
            throw new NoContentException(
                    "Business with id " + dto.getBusinessId() + " does not exists",
                    21);
        }
        if (dto.getCapacity() < 1) {
            throw new BadRequestException(
                    "Branch capacity must be greater than 0",
                    22);
        }

        Branch newBranch = branchMapper.toEntity(dto, business.get());
        newBranch = branchRepository.save(newBranch);

        BranchDTO dtoResponse = branchMapper.toDTO(newBranch);

        return dtoResponse;
    }

    public BranchDTO update(Long id, BranchDTO dto)
            throws NoContentException, BadRequestException {
        Optional<Branch> current = branchRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        if (dto.getCapacity() < 1) {
            throw new BadRequestException(
                    "Branch capacity must be greater than 0",
                    21);
        }

        Branch newBranch = branchMapper.updateModel(dto, current.get());
        newBranch = branchRepository.save(newBranch);
        BranchDTO dtoResponse = branchMapper.toDTO(newBranch);

        return dtoResponse;
    }

    public void delete(Long id) throws NoContentException {
        Optional<Branch> current = branchRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }
        branchRepository.deleteById(id);
    }

    public ProductSubCategoryListDTO getProductSubCategories(
            Long branchId,
            Long productCategoryId) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }
        Optional<ProductCategory> category = productCategoryRepository.findById(productCategoryId);
        if (category.isEmpty()) {
            throw new NoContentException(
                    "Product category with id " + productCategoryId + " does not exists",
                    24);
        }

        List<ProductSubCategoryDTO> response = new ArrayList<>();
        productSubCategoryRepository.findAllByBranchIdAndCategoryId(branchId, productCategoryId)
                .forEach(subCategory -> {
                    ProductSubCategoryDTO dto = productSubCategoryMapper.toDTO(subCategory);
                    response.add(dto);
                });

        return ProductSubCategoryListDTO.builder().productSubCategories(response).build();
    }

    public ProductListDTO getProducts(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        List<ProductDTO> response = new ArrayList<>();
        productRepository.findAllBySubCategory_Branch_Id(id)
                .forEach(product -> {
                    ProductDTO dto = productMapper.toDTO(product);
                    response.add(dto);
                });

        return ProductListDTO.builder().products(response).build();
    }

    public PromotionListDTO getPromotions(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        List<PromotionDTO> response = new ArrayList<>();
        promotionRepository.findAllByBranchId(id).forEach(promotion -> {
            PromotionDTO dto = promotionMapper.toDTO(promotion);
            response.add(dto);
        });

        return PromotionListDTO.builder().promotions(response).build();
    }

    public ReservationListDTO getReservations(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAllByBranchId(id).forEach(reservation -> {
            ReservationDTO dto = reservationMapper.toDTO(reservation);
            response.add(dto);
        });

        // Complete reservations
        List<ReservationDTO> result = response.stream().map(reservation -> {
            reservation.completeData(guestRepository, clientGroupRepository, clientRepository);
            return reservation;
        }).collect(Collectors.toList());

        return ReservationListDTO.builder().reservations(result).build();
    }

    public ReservationListDTO getReservationsByDate(Long id, Date reservationDate)
            throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAllByBranchIdAndReservationDateGreaterThanEqual(id, reservationDate)
                .forEach(reservation -> {
                    ReservationDTO dto = reservationMapper.toDTO(reservation);
                    response.add(dto);
                });

        // Complete reservations
        List<ReservationDTO> result = response.stream().map(reservation -> {
            reservation.completeData(guestRepository, clientGroupRepository, clientRepository);
            return reservation;
        }).collect(Collectors.toList());

        return ReservationListDTO.builder().reservations(result).build();
    }

    public ClientListDTO getFavoriteClients(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        List<ClientDTO> response = new ArrayList<>();
        favoriteBranchRepository.findAllByClientId(id).forEach(fav -> {
            ClientDTO dto = clientMapper.toDTO(fav.getClient());
            dto.setUserId(fav.getClient().getUser().getId());
            response.add(dto);
        });

        return ClientListDTO.builder().clients(response).build();
    }

    public ReviewListDTO getReviews(Long id) {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        List<ReviewDTO> response = new ArrayList<>();
        reviewRepository.findAllByBranchId(id)
                .forEach(review -> {
                    ReviewDTO dto = reviewMapper.toDTO(review);
                    response.add(dto);
                });

        return ReviewListDTO.builder().reviews(response).build();
    }

    // Nows lets make the pagination of branches
    // This method returns a list of branches with pagination

    public BranchListDTO getBranchesPage(int page,
            int size,
            String sorting_by,
            boolean ascending,
            BigDecimal min_reservation_price,
            BigDecimal max_reservation_price,
            Float min_score,
            int min_capacity) throws UnprocessableException, NoContentException {

        // Now lets add the exeption handling
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    40);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Size cannot be less than one",
                    41);
        }

        // Check if sorting_by is in BranchStatics.BranchSortingKeys
        if (!BranchStatics.BranchSortingKeys.contains(sorting_by)) {
            throw new UnprocessableException(
                    "Sorting key is not valid",
                    42);
        }

        // Create a Pageable object that specifies the page and size parameters as well
        // as a sort
        // order for the results
        Pageable paging;
        if (ascending) {
            paging = PageRequest.of(
                    page,
                    size,
                    Sort.by(sorting_by).ascending());
        } else {
            paging = PageRequest.of(
                    page,
                    size,
                    Sort.by(sorting_by).descending());
        }

        if (min_reservation_price == null) {
            min_reservation_price = BigDecimal.valueOf(0L);
        }
        if (max_reservation_price == null) {
            max_reservation_price = BigDecimal.valueOf(Long.MAX_VALUE);
        }
        if (min_score == null) {
            min_score = 0.0f;
        }
        if (min_capacity == 0) {
            min_capacity = 1;
        }

        // Lets apply the filters
        Page<Branch> pagedResult = branchRepository
                .findAllByReservationPriceBetweenAndScoreGreaterThanEqualAndCapacityGreaterThanEqual(
                        min_reservation_price,
                        max_reservation_price,
                        min_score,
                        min_capacity,
                        paging);

        List<BranchDTO> response = new ArrayList<>();

        pagedResult.forEach(branch -> {
            BranchDTO dto = branchMapper.toDTO(branch);
            response.add(dto);
        });

        return BranchListDTO.builder().branches(response).build();
    }

    // This method returns a list of reviews with pagination
    public ReviewListDTO getReviewsPage(Long id, int page, int size) throws UnprocessableException, NoContentException {

        // Now lets add the exeption handling
        // TODO: Add the corresponding codes to the exceptions
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    40);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Size cannot be less than one",
                    41);
        }

        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        // Create a Pageable object that specifies the page and size parameters as well
        // as a sort
        // order for the results
        Pageable paging = PageRequest.of(
                page,
                size,
                // Sort by id descending
                // TODO: Add more ways to sort
                Sort.by("id").descending());

        // Query the database for the appropriate page of results using the findAll
        // method of the
        // reviewRepository
        Page<Review> pagedResult = reviewRepository.findAllByBranchId(id, paging);// .findAll(paging);

        // Map the results to a list of ReviewDTO objects using the reviewMapper
        List<ReviewDTO> response = new ArrayList<>();

        pagedResult.forEach(review -> {
            ReviewDTO dto = reviewMapper.toDTO(review);
            dto.setLikes(reviewLikeRepository.findAllByReviewId(review.getId()).size());
            response.add(dto);
        });

        // Return a ReviewListDTO object that contains the list of ReviewDTO objects
        return ReviewListDTO.builder().reviews(response).build();
    }

    // This method returns a page of reservations with pagination
    public ReservationListDTO getReservationsPage(Long id, Date reservationDate, int page, int size)
            throws UnprocessableException, NoContentException {

        // Now lets add the exeption handling
        // TODO: Add the corresponding codes to the exceptions
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    40);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Size cannot be less than one",
                    41);
        }

        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        // Create a Pageable object that specifies the page and size parameters as well
        // as a sort
        // order for the results
        Pageable paging = PageRequest.of(
                page,
                size,
                // Sort by id descending
                Sort.by("reservationDate").descending());

        // Query the database for the appropriate page of results using the findAll
        // method of the reservation repository
        Page<Reservation> pagedResult = reservationRepository.findAllByBranchIdAndReservationDateGreaterThanEqual(id,
                reservationDate, paging);// .findAll(paging);

        // Map the results to a list of ReservationDTO objects using the
        // ReservationMapper
        List<ReservationDTO> response = new ArrayList<>();

        pagedResult.forEach(reservation -> {
            ReservationDTO dto = reservationMapper.toDTO(reservation);
            response.add(dto);
        });

        // Complete reservations
        List<ReservationDTO> result = response.stream().map(reservation -> {
            reservation.completeData(guestRepository, clientGroupRepository, clientRepository);
            return reservation;
        }).collect(Collectors.toList());

        // Return a ReservationListDTO object that contains the list of ReservationDTO
        // objects
        return ReservationListDTO.builder().reservations(result).build();
    }
}
