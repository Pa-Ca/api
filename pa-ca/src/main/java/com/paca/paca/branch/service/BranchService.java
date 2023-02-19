package com.paca.paca.branch.service;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.promotion.utils.PromotionMapper;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.reservation.utils.ReservationMapper;

import lombok.RequiredArgsConstructor;

import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.product_sub_category.utils.ProductSubCategoryMapper;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchMapper branchMapper;

    private final ProductMapper productMapper;

    private final PromotionMapper promotionMapper;

    private final ReservationMapper reservationMapper;

    private final ProductSubCategoryMapper productSubCategoryMapper;

    private final BranchRepository branchRepository;

    private final ProductRepository productRepository;

    private final BusinessRepository businessRepository;

    private final PromotionRepository promotionRepository;

    private final ReservationRepository reservationRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductSubCategoryRepository productSubCategoryRepository;

    public ResponseEntity<BranchListDTO> getAll() {
        List<BranchDTO> response = new ArrayList<>();
        branchRepository.findAll().forEach(branch -> {
            BranchDTO dto = branchMapper.toDTO(branch);
            response.add(dto);
        });

        return ResponseEntity.ok(BranchListDTO.builder().branches(response).build());
    }

    public ResponseEntity<BranchDTO> getById(Long id) throws NoContentException {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Branch with id " + id + " does not exists",
                        20));

        BranchDTO dto = branchMapper.toDTO(branch);
        return new ResponseEntity<BranchDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<BranchDTO> save(BranchDTO branchDto) throws NoContentException, BadRequestException {
        Optional<Business> business = businessRepository.findById(branchDto.getBusinessId());
        if (business.isEmpty()) {
            throw new NoContentException(
                    "Business with id " + branchDto.getBusinessId() + " does not exists",
                    21);
        }
        if (branchDto.getCapacity() < 1) {
            throw new BadRequestException(
                    "Branch capacity must be greater than 0",
                    22);
        }

        Branch newBranch = branchMapper.toEntity(branchDto);
        newBranch.setBusiness(business.get());
        newBranch = branchRepository.save(newBranch);

        BranchDTO newDto = branchMapper.toDTO(newBranch);

        return new ResponseEntity<BranchDTO>(newDto, HttpStatus.OK);
    }

    public ResponseEntity<BranchDTO> update(Long id, BranchDTO branchDto)
            throws NoContentException, BadRequestException {
        Optional<Branch> current = branchRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }

        if (branchDto.getCapacity() < 1) {
            throw new BadRequestException(
                    "Branch capacity must be greater than 0",
                    21);
        }

        Branch newBranch = branchMapper.updateModel(branchDto, current.get());
        newBranch = branchRepository.save(newBranch);
        BranchDTO newDto = branchMapper.toDTO(newBranch);

        return new ResponseEntity<BranchDTO>(newDto, HttpStatus.OK);
    }

    public void delete(Long id) throws NoContentException {
        Optional<Branch> current = branchRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }
        branchRepository.deleteById(id);
    }

    public ResponseEntity<ProductSubCategoryListDTO> getProductSubCategories(
            Long branchId,
            Long productCategoryId) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + branchId + " does not exists",
                    20);
        }
        Optional<ProductCategory> category = productCategoryRepository.findById(productCategoryId);
        if (category.isEmpty()) {
            throw new NoContentException(
                    "Product category with id: " + productCategoryId + " does not exists",
                    24);
        }

        List<ProductSubCategoryDTO> response = new ArrayList<>();
        productSubCategoryRepository.findAllByBranchIdAndCategoryId(branchId, productCategoryId)
                .forEach(subCategory -> {
                    ProductSubCategoryDTO dto = productSubCategoryMapper.toDTO(subCategory);
                    response.add(dto);
                });

        return ResponseEntity.ok(ProductSubCategoryListDTO.builder().productSubCategories(response).build());
    }

    public ResponseEntity<ProductListDTO> getProducts(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }

        List<ProductDTO> response = new ArrayList<>();
        productRepository.findAllBySubCategory_Branch_Id(id)
                .forEach(product -> {
                    ProductDTO dto = productMapper.toDTO(product);
                    response.add(dto);
                });

        return ResponseEntity.ok(ProductListDTO.builder().products(response).build());
    }

    public ResponseEntity<PromotionListDTO> getPromotions(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }

        List<PromotionDTO> response = new ArrayList<>();
        promotionRepository.findAllByBranchId(id).forEach(promotion -> {
            PromotionDTO dto = promotionMapper.toDTO(promotion);
            response.add(dto);
        });

        return ResponseEntity.ok(PromotionListDTO.builder().promotions(response).build());
    }

    public ResponseEntity<ReservationListDTO> getReservations(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }

        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAllByBranchId(id).forEach(reservation -> {
            ReservationDTO dto = reservationMapper.toDTO(reservation);
            response.add(dto);
        });

        return ResponseEntity.ok(ReservationListDTO.builder().reservations(response).build());
    }

    public ResponseEntity<ReservationListDTO> getReservationsByDate(Long id, Date reservationDate)
            throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }

        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAllByBranchIdAndReservationDateGreaterThanEqual(id, reservationDate)
                .forEach(reservation -> {
                    ReservationDTO dto = reservationMapper.toDTO(reservation);
                    response.add(dto);
                });

        return ResponseEntity.ok(ReservationListDTO.builder().reservations(response).build());
    }
}
