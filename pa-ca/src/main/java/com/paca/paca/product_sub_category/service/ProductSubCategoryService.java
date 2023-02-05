package com.paca.paca.product_sub_category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;
import com.paca.paca.product_sub_category.utils.ProductSubCategoryMapper;

public class ProductSubCategoryService {

    private final ProductSubCategoryMapper productSubCategoryMapper;

    private final ProductSubCategoryRepository productSubCategoryRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final BranchRepository branchRepository;

    public ProductSubCategoryService(
            ProductSubCategoryMapper productSubCategoryMapper,
            ProductSubCategoryRepository productSubCategoryRepository,
            ProductCategoryRepository productCategoryRepository,
            BranchRepository branchRepository) {
        this.productSubCategoryMapper = productSubCategoryMapper;
        this.productSubCategoryRepository = productSubCategoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.branchRepository = branchRepository;
    }

    public ResponseEntity<ProductSubCategoryListDTO> getAll() {
        List<ProductSubCategoryDTO> response = new ArrayList<>();
        productSubCategoryRepository.findAll().forEach(category -> {
            ProductSubCategoryDTO dto = productSubCategoryMapper.toDTO(category);
            dto.setBranchId(category.getBranch().getId());
            dto.setProductCategoryId(category.getProductCategory().getId());
            response.add(dto);
        });

        return ResponseEntity.ok(ProductSubCategoryListDTO.builder().categories(response).build());
    }

    public ResponseEntity<ProductSubCategoryDTO> getById(Long id) throws NoContentException {
        ProductSubCategory category = productSubCategoryRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Product sub-category with id " + id + " does not exists",
                        23));

        ProductSubCategoryDTO dto = productSubCategoryMapper.toDTO(category);
        dto.setBranchId(category.getBranch().getId());
        dto.setProductCategoryId(category.getProductCategory().getId());
        return new ResponseEntity<ProductSubCategoryDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<ProductSubCategoryDTO> save(ProductSubCategoryDTO productSubCategoryDTO)
            throws NoContentException, BadRequestException {
        Optional<Branch> branch = branchRepository.findById(productSubCategoryDTO.getBranchId());
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + productSubCategoryDTO.getBranchId() + " does not exists",
                    20);
        }
        Optional<ProductCategory> category = productCategoryRepository
                .findById(productSubCategoryDTO.getProductCategoryId());
        if (category.isEmpty()) {
            throw new NoContentException(
                    "Product category with id " + productSubCategoryDTO.getProductCategoryId() + " does not exists",
                    24);
        }

        ProductSubCategory subCategory = productSubCategoryMapper.toEntity(productSubCategoryDTO);
        subCategory.setBranch(branch.get());
        subCategory.setProductCategory(category.get());
        productSubCategoryRepository.save(subCategory);

        ProductSubCategoryDTO newDto = productSubCategoryMapper.toDTO(subCategory);
        newDto.setBranchId(subCategory.getBranch().getId());
        newDto.setProductCategoryId(subCategory.getProductCategory().getId());

        return new ResponseEntity<ProductSubCategoryDTO>(newDto, HttpStatus.OK);
    }

    public ResponseEntity<ProductSubCategoryDTO> update(Long id, ProductSubCategoryDTO productSubCategoryDTO)
            throws NoContentException, BadRequestException {
        Optional<ProductSubCategory> current = productSubCategoryRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id: " + id + " does not exists",
                    23);
        }

        ProductSubCategory newProductSubCategory = productSubCategoryMapper.updateModel(
                current.get(),
                productSubCategoryDTO);
        newProductSubCategory = productSubCategoryRepository.save(newProductSubCategory);
        ProductSubCategoryDTO newDto = productSubCategoryMapper.toDTO(newProductSubCategory);
        newDto.setBranchId(newProductSubCategory.getBranch().getId());
        newDto.setProductCategoryId(newProductSubCategory.getProductCategory().getId());

        return new ResponseEntity<ProductSubCategoryDTO>(newDto, HttpStatus.OK);
    }

    public void delete(Long id) throws NoContentException {
        Optional<ProductSubCategory> current = productSubCategoryRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id: " + id + " does not exists",
                    23);
        }
        productSubCategoryRepository.deleteById(id);
    }
}
