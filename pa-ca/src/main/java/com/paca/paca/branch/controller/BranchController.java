package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(BranchStatics.Endpoint.PATH)
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public ResponseEntity<BranchListDTO> getAll() {
        return branchService.getAll();
    }

    @PostMapping
    public ResponseEntity<BranchDTO> save(@RequestBody BranchDTO branchDto)
            throws NoContentException, BadRequestException {
        return branchService.save(branchDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return branchService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(
            @PathVariable("id") Long id,
            @RequestBody BranchDTO branchDto)
            throws NoContentException, BadRequestException {
        return branchService.update(id, branchDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        branchService.delete(id);
    }

    @GetMapping("/{branchId}/product-category/{productCategoryId}/product-sub-category")
    public ResponseEntity<ProductSubCategoryListDTO> getProductSubCategories(
            @PathVariable("branchId") Long branchId,
            @PathVariable("productCategoryId") Long productCategoryId) throws NoContentException {
        return branchService.getProductSubCategories(branchId, productCategoryId);
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<ProductListDTO> getProducts(Long id) throws NoContentException {
        return branchService.getProducts(id);
    }
}
