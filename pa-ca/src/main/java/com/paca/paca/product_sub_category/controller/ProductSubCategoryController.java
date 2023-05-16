package com.paca.paca.product_sub_category.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.product_sub_category.dto.ProductCategoryListDTO;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.product_sub_category.service.ProductSubCategoryService;
import com.paca.paca.product_sub_category.statics.ProductSubCategoryStatics;
import com.paca.paca.product_sub_category.utils.ValidateProductSubCategoryOwnerInterceptor.ValidateProductSubCategoryOwner;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ProductSubCategoryStatics.Endpoint.PATH)
@Tag(name = "09. Product Sub-Category", description = "Product Sub-Category Management Controller")
public class ProductSubCategoryController {

    private final ProductSubCategoryService productSubCategoryService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<ProductSubCategoryListDTO> getAll() {
        return ResponseEntity.ok(productSubCategoryService.getAll());
    }

    @GetMapping("/categories")
    public ResponseEntity<ProductCategoryListDTO> getAllProductCategories() {
        return ResponseEntity.ok(productSubCategoryService.getAllProductCategories());
    }

    @PostMapping
    @ValidateRoles({ "business" })
    public ResponseEntity<ProductSubCategoryDTO> save(
            @RequestBody ProductSubCategoryDTO productSubCategoryDTO)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(productSubCategoryService.save(productSubCategoryDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSubCategoryDTO> getById(
            @PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(productSubCategoryService.getById(id));
    }

    @PutMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateProductSubCategoryOwner
    public ResponseEntity<ProductSubCategoryDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ProductSubCategoryDTO productSubCategoryDTO)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(productSubCategoryService.update(id, productSubCategoryDTO));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateProductSubCategoryOwner
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        productSubCategoryService.delete(id);
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<ProductListDTO> getAllProducts(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(productSubCategoryService.getAllProducts(id));
    }
}
