package com.paca.paca.sale.controller;

import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.sale.utils.ValidateSaleProductOwnerInterceptor.ValidateSaleProductOwner;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;

import com.paca.paca.sale.statics.SaleProductStatics;
import com.paca.paca.sale.service.SaleProductService;
import com.paca.paca.sale.dto.SaleProductDTO;



//import BigDecimal


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import lombok.RequiredArgsConstructor;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(SaleProductStatics.Endpoint.PATH)
@Tag(name = "01. SaleProduct", description = "SaleProduct Controller")
public class SaleProductController {

    private final SaleProductService saleProductService;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new sale product", description = "Create a new SaleProduct")
    public ResponseEntity<SaleProductDTO> save(@RequestBody SaleProductDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(saleProductService.save(dto));
    }
    

    @PutMapping("/{id}") // Hacer otro interceptor para validar que el usuario sea el dueño del negocio
    @ValidateRoles({ "business" })
    @ValidateSaleProductOwner
    @Operation(summary = "Updates a sale product", description = "Updates the data of a SaleProduct given its ID and DTO")
    public ResponseEntity<SaleProductDTO> update(
            @PathVariable("id") Long id,
            @RequestBody SaleProductDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(saleProductService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateSaleProductOwner
    @Operation(summary = "Deletes a sale product", description = "Delete the data of a SaleProduct given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        saleProductService.delete(id);
    }
}
