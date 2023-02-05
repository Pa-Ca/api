package com.paca.paca.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

public class ProductService {

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    private final ProductSubCategoryRepository productSubCategoryRepository;

    public ProductService(
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductSubCategoryRepository productSubCategoryRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productSubCategoryRepository = productSubCategoryRepository;
    }

    public ResponseEntity<ProductListDTO> getAll() {
        List<ProductDTO> response = new ArrayList<>();
        productRepository.findAll().forEach(product -> {
            ProductDTO dto = productMapper.toDTO(product);
            dto.setProductSubCategoryId(product.getSubCategory().getId());
            response.add(dto);
        });

        return ResponseEntity.ok(ProductListDTO.builder().products(response).build());
    }

    public ResponseEntity<ProductDTO> getById(Long id) throws NoContentException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Product with id " + id + " does not exists",
                        25));

        ProductDTO dto = productMapper.toDTO(product);
        dto.setProductSubCategoryId(product.getSubCategory().getId());
        return new ResponseEntity<ProductDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<ProductDTO> save(ProductDTO productDto) throws NoContentException {
        Optional<ProductSubCategory> subCategory = productSubCategoryRepository
                .findById(productDto.getProductSubCategoryId());
        if (subCategory.isEmpty()) {
            throw new NoContentException(
                    "Product sub-category with id " + productDto.getProductSubCategoryId() + " does not exists",
                    23);
        }

        Product newProduct = productMapper.toEntity(productDto);
        newProduct.setSubCategory(subCategory.get());
        newProduct = productRepository.save(newProduct);

        ProductDTO newDto = productMapper.toDTO(newProduct);
        newDto.setProductSubCategoryId(newProduct.getSubCategory().getId());

        return new ResponseEntity<ProductDTO>(newDto, HttpStatus.OK);
    }

    public ResponseEntity<ProductDTO> update(Long id, ProductDTO productDto) throws NoContentException {
        Optional<Product> current = productRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Product with id: " + id + " does not exists",
                    25);
        }

        Product newProduct = productMapper.updateModel(current.get(), productDto);
        newProduct = productRepository.save(newProduct);
        ProductDTO newDto = productMapper.toDTO(newProduct);
        newDto.setProductSubCategoryId(newProduct.getSubCategory().getId());

        return new ResponseEntity<ProductDTO>(newDto, HttpStatus.OK);
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
