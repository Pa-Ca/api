package com.paca.paca.product.controller;

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

import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.service.ProductService;
import com.paca.paca.product.statics.ProductStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.product.utils.ValidateProductOwnerInterceptor.ValidateProductOwner;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ProductStatics.Endpoint.PATH)
@Tag(name = "11. Product", description = "Product Management Controller")
public class ProductController {

    private final ProductService productService;

    @ValidateRoles({ "business" })
    @PostMapping(ProductStatics.Endpoint.SAVE)
    @Operation(summary = "Create new product", description = "Create a new product in the app")
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO dto)
            throws NotFoundException, ConflictException {
        return ResponseEntity.ok(productService.save(dto));
    }

    @GetMapping(ProductStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get product by ID", description = "Gets the data of a product given its ID")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(productService.getById(id));
    }

    @ValidateProductOwner
    @ValidateRoles({ "business" })
    @PutMapping(ProductStatics.Endpoint.UPDATE)
    @Operation(summary = "Update product", description = "Updates the data of a product given its ID")
    public ResponseEntity<ProductDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ProductDTO dto)
            throws NotFoundException, ConflictException {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @ValidateProductOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(ProductStatics.Endpoint.DELETE)
    @Operation(summary = "Delete product", description = "Delete the data of a product given its ID")
    public void delete(@PathVariable("id") Long id) throws NotFoundException {
        productService.delete(id);
    }
}
