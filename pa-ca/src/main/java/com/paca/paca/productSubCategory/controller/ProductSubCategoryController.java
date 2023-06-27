package com.paca.paca.productSubCategory.controller;

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
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.dto.ProductCategoryListDTO;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.productSubCategory.service.ProductSubCategoryService;
import com.paca.paca.productSubCategory.statics.ProductSubCategoryStatics;
import com.paca.paca.productSubCategory.utils.ValidateProductSubCategoryOwnerInterceptor.ValidateProductSubCategoryOwner;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ProductSubCategoryStatics.Endpoint.PATH)
@Tag(name = "10. Product Sub-Category", description = "Product Sub-Category Management Controller")
public class ProductSubCategoryController {

    private final ProductSubCategoryService productSubCategoryService;

    @GetMapping
    @ValidateRoles({})
    @Operation(summary = "Get all product sub-categories", description = "Returns a list with all product sub-categories")
    public ResponseEntity<ProductSubCategoryListDTO> getAll() {
        return ResponseEntity.ok(productSubCategoryService.getAll());
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all product categories", description = "Returns a list with all product categories")
    public ResponseEntity<ProductCategoryListDTO> getAllProductCategories() {
        return ResponseEntity.ok(productSubCategoryService.getAllProductCategories());
    }

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new product sub-category", description = "Create a new product sub-category in the app")
    public ResponseEntity<ProductSubCategoryDTO> save(
            @RequestBody ProductSubCategoryDTO productSubCategoryDTO)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(productSubCategoryService.save(productSubCategoryDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product sub-category by ID", description = "Gets the data of a product sub-category given its ID")
    public ResponseEntity<ProductSubCategoryDTO> getById(
            @PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(productSubCategoryService.getById(id));
    }

    @PutMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateProductSubCategoryOwner
    @Operation(summary = "Update product sub-category", description = "Updates the data of a product sub-category given its ID")
    public ResponseEntity<ProductSubCategoryDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ProductSubCategoryDTO productSubCategoryDTO)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(productSubCategoryService.update(id, productSubCategoryDTO));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateProductSubCategoryOwner
    @Operation(summary = "Delete product sub-category", description = "Delete the data of a product sub-category given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        productSubCategoryService.delete(id);
    }

    @GetMapping("/{id}/product")
    @Operation(summary = "Get all products of a sub-category", description = "Gets a list with the data of all the products of a sub-category given its id")
    public ResponseEntity<ProductListDTO> getAllProducts(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(productSubCategoryService.getAllProducts(id));
    }
}
