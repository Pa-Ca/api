package com.paca.paca.product.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    private final ProductSubCategoryRepository productSubCategoryRepository;

    public ProductListDTO getAll() {
        List<ProductDTO> response = new ArrayList<>();
        productRepository.findAll().forEach(product -> {
            ProductDTO dto = productMapper.toDTO(product);
            response.add(dto);
        });

        return ProductListDTO.builder().products(response).build();
    }

    public ProductDTO getById(Long id) throws NoContentException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Product with id " + id + " does not exists",
                        25));

        ProductDTO dto = productMapper.toDTO(product);
        return dto;
    }

    public ProductDTO save(ProductDTO dto) throws NoContentException, ConflictException {
        Optional<ProductSubCategory> subCategory = productSubCategoryRepository
                .findById(dto.getSubCategoryId());
        if (subCategory.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id " + dto.getSubCategoryId() + " does not exists",
                    23);
        }
        if (productRepository.existsBySubCategoryIdAndName(dto.getSubCategoryId(), dto.getName())) {
            throw new NoContentException(
                    "Product with name " + dto.getName() + " already exists",
                    57);
        }

        Product newProduct = productMapper.toEntity(dto, subCategory.get());
        newProduct = productRepository.save(newProduct);

        ProductDTO dtoResponse = productMapper.toDTO(newProduct);

        return dtoResponse;
    }

    public ProductDTO update(Long id, ProductDTO dto) throws NoContentException, ConflictException {
        Optional<Product> current = productRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product with id: " + id + " does not exists",
                    25);
        }
        if (dto.getName() != null
                && productRepository.existsBySubCategoryIdAndName(dto.getSubCategoryId(), dto.getName())) {
            throw new NoContentException(
                    "Product with name " + dto.getName() + " already exists",
                    57);
        }

        Product newProduct = productMapper.updateModel(dto, current.get());
        newProduct = productRepository.save(newProduct);
        ProductDTO dtoResponse = productMapper.toDTO(newProduct);

        return dtoResponse;
    }

    public void delete(Long id) throws NoContentException {
        Optional<Product> current = productRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product with id: " + id + " does not exists",
                    25);
        }
        productRepository.deleteById(id);
    }
}
