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
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.product.service.ProductService;
import com.paca.paca.product.statics.ProductStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.product.utils.ValidateProductOwnerInterceptor.ValidateProductOwner;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(ProductStatics.Endpoint.PATH)
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<ProductListDTO> getAll() {
        return productService.getAll();
    }

    @PostMapping
    @ValidateRoles({"business"})
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO dto) throws NoContentException {
        return productService.save(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    @ValidateProductOwner
    @ValidateRoles({ "business" })
    public ResponseEntity<ProductDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ProductDTO dto)
            throws NoContentException {
        return productService.update(id, dto);
    }

    @ValidateProductOwner
    @DeleteMapping("/{id}")
    @ValidateRoles({"business"})
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        productService.delete(id);
    }
}
