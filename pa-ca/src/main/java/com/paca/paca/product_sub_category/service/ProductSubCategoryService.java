package com.paca.paca.product_sub_category.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.product_sub_category.dto.ProductCategoryDTO;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.product_sub_category.dto.ProductCategoryListDTO;
import com.paca.paca.product_sub_category.utils.ProductCategoryMapper;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.product_sub_category.utils.ProductSubCategoryMapper;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSubCategoryService {

    private final ProductSubCategoryMapper productSubCategoryMapper;

    private final ProductCategoryMapper productCategoryMapper;

    private final ProductMapper productMapper;

    private final ProductSubCategoryRepository productSubCategoryRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final BranchRepository branchRepository;

    private final ProductRepository productRepository;

    public ProductSubCategoryListDTO getAll() {
        List<ProductSubCategoryDTO> response = new ArrayList<>();
        productSubCategoryRepository.findAll().forEach(category -> {
            ProductSubCategoryDTO dto = productSubCategoryMapper.toDTO(category);
            response.add(dto);
        });

        return ProductSubCategoryListDTO.builder().productSubCategories(response).build();
    }

    public ProductCategoryListDTO getAllProductCategories() {
        List<ProductCategoryDTO> response = new ArrayList<>();
        productCategoryRepository.findAll().forEach(category -> {
            ProductCategoryDTO dto = productCategoryMapper.toDTO(category);
            response.add(dto);
        });

        return ProductCategoryListDTO.builder().productCategories(response).build();
    }

    public ProductSubCategoryDTO getById(Long id) throws NoContentException {
        ProductSubCategory category = productSubCategoryRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Product sub-category with id " + id + " does not exists",
                        23));

        ProductSubCategoryDTO dto = productSubCategoryMapper.toDTO(category);
        return dto;
    }

    public ProductSubCategoryDTO save(ProductSubCategoryDTO dto)
            throws NoContentException, BadRequestException {
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }
        Optional<ProductCategory> category = productCategoryRepository
                .findById(dto.getCategoryId());
        if (category.isEmpty()) {
            throw new NoContentException(
                    "Product category with id " + dto.getCategoryId() + " does not exists",
                    24);
        }

        ProductSubCategory subCategory = productSubCategoryMapper.toEntity(
                dto,
                branch.get(),
                category.get());
        productSubCategoryRepository.save(subCategory);

        ProductSubCategoryDTO dtoResponse = productSubCategoryMapper.toDTO(subCategory);

        return dtoResponse;
    }

    public ProductSubCategoryDTO update(Long id, ProductSubCategoryDTO dto)
            throws NoContentException, BadRequestException {
        Optional<ProductSubCategory> current = productSubCategoryRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id " + id + " does not exists",
                    23);
        }

        ProductSubCategory newProductSubCategory = productSubCategoryMapper.updateModel(
                dto,
                current.get());
        newProductSubCategory = productSubCategoryRepository.save(newProductSubCategory);
        ProductSubCategoryDTO dtoResponse = productSubCategoryMapper.toDTO(newProductSubCategory);

        return dtoResponse;
    }

    public void delete(Long id) throws NoContentException {
        Optional<ProductSubCategory> current = productSubCategoryRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id " + id + " does not exists",
                    23);
        }
        productSubCategoryRepository.deleteById(id);
    }

    public ProductListDTO getAllProducts(Long id) throws NoContentException {
        Optional<ProductSubCategory> category = productSubCategoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id " + id + " does not exists",
                    23);
        }

        List<ProductDTO> response = new ArrayList<>();
        productRepository.findAllBySubCategoryId(id).forEach(product -> {
            ProductDTO dto = productMapper.toDTO(product);
            response.add(dto);
        });

        return ProductListDTO.builder().products(response).build();
    }
}
