package com.paca.paca.branch.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.dto.TaxListDTO;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.TableListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.utils.TableMapper;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.ReviewMapper;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.branch.dto.PaymentOptionListDTO;
import com.paca.paca.promotion.utils.PromotionMapper;
import com.paca.paca.branch.utils.PaymentOptionMapper;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.productSubCategory.utils.ProductSubCategoryMapper;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final TableMapper tableMapper;

    private final ClientMapper clientMapper;

    private final BranchMapper branchMapper;

    private final ReviewMapper reviewMapper;

    private final TaxMapper defaultTaxMapper;

    private final ProductMapper productMapper;

    private final PromotionMapper promotionMapper;

    private final PaymentOptionMapper paymentOptionMapper;

    private final ProductSubCategoryMapper productSubCategoryMapper;

    private final TableRepository tableRepository;

    private final ReviewRepository reviewRepository;

    private final BranchRepository branchRepository;

    private final ProductRepository productRepository;

    private final BusinessRepository businessRepository;

    private final PromotionRepository promotionRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final DefaultTaxRepository defaultTaxRepository;

    private final PaymentOptionRepository paymentOptionRepository;

    private final FavoriteBranchRepository favoriteBranchRepository;

    private final ProductSubCategoryRepository productSubCategoryRepository;

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

        Branch branch = current.get();
        branch.setDeleted(true);
        branchRepository.save(branch);
    }

    public ProductSubCategoryListDTO getProductSubCategories(Long branchId) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        List<ProductSubCategoryDTO> response = new ArrayList<>();
        productSubCategoryRepository.findAllByBranchId(branchId)
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

    public ReviewListDTO getReviewsPage(Long id, int page, int size) throws UnprocessableException, NoContentException {
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    44);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Page size cannot be less than one",
                    45);
        }

        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        Pageable paging = PageRequest.of(
                page,
                size,
                Sort.by("id").descending());
        Page<Review> pagedResult = reviewRepository.findAllByBranchId(id, paging);

        List<ReviewDTO> response = new ArrayList<>();
        pagedResult.forEach(review -> {
            ReviewDTO dto = reviewMapper.toDTO(review);
            dto.setLikes(reviewLikeRepository.findAllByReviewId(review.getId()).size());
            response.add(dto);
        });

        // Return a ReviewListDTO object that contains the list of ReviewDTO objects
        return ReviewListDTO.builder().reviews(response).build();
    }

    public TaxListDTO getDefaultTaxesByBranchId(Long branchId) {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        List<Tax> defaultTaxes = defaultTaxRepository.findAllByBranchId(branchId);
        List<TaxDTO> defaultTaxesDTO = new ArrayList<>();
        for (Tax defaultTax : defaultTaxes) {
            defaultTaxesDTO.add(defaultTaxMapper.toDTO(defaultTax));
        }

        return TaxListDTO.builder()
                .taxes(defaultTaxesDTO)
                .build();
    }

    public TableListDTO getTablesByBranchId(Long branchId) {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        // Get the tables
        List<Table> tables = tableRepository.findAllByBranchId(branchId);

        List<TableDTO> tablesDTO = new ArrayList<>();

        for (Table table : tables) {
            tablesDTO.add(tableMapper.toDTO(table));
        }

        return TableListDTO.builder()
                .tables(tablesDTO)
                .build();

    }

    public PaymentOptionListDTO getPaymentOptionsByBranchId(Long branchId) {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        List<PaymentOption> paymentOptions = paymentOptionRepository.findAllByBranchId(branchId);

        List<PaymentOptionDTO> paymentOptionsDTO = new ArrayList<>();
        for (PaymentOption paymentOption : paymentOptions) {
            paymentOptionsDTO.add(paymentOptionMapper.toDTO(paymentOption));
        }

        return PaymentOptionListDTO.builder()
                .paymentOptions(paymentOptionsDTO)
                .build();
    }

}
