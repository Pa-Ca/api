package com.paca.paca.sale.controller;

import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.dto.SaleDTO;

import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.sale.utils.ValidateSaleOwnerInterceptor.ValidateSaleOwner;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;

//import BigDecimal

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(SaleStatics.Endpoint.PATH)
@Tag(name = "01. Sale", description = "Sale Management Controller")
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new sale", description = "Create a new sale")
    public ResponseEntity<SaleDTO> save(@RequestBody SaleDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(saleService.save(dto));
    }

    @PutMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateSaleOwner
    @Operation(summary = "Update sale", description = "Updates the data of a sale given its ID and DTO")
    public ResponseEntity<SaleDTO> update(
            @PathVariable("id") Long id,
            @RequestBody SaleDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(saleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateSaleOwner
    @Operation(summary = "Delete table", description = "Delete the data of a table given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        saleService.delete(id);
    }

    // Clear sales
    @Transactional
    @DeleteMapping("/{id}/clear")
    @ValidateRoles({ "business" })
    @ValidateSaleOwner
    @Operation(summary = "Delete sale products form a sale", description = "Deletes all the sales products of a sale givent the slae ID")
    public void clearSaleProducts(@PathVariable("id") Long id) throws NoContentException, BadRequestException {
        saleService.clearSaleProducts(id);
    }

}
