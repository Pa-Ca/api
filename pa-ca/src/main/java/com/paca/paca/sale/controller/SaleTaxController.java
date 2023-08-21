package com.paca.paca.sale.controller;

import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.dto.SaleTaxDTO;
import com.paca.paca.sale.statics.SaleTaxStatics;
import com.paca.paca.sale.service.SaleTaxService;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.sale.utils.ValidateSaleTaxOwnerInterceptor.ValidateSaleTaxOwner;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(SaleTaxStatics.Endpoint.PATH)
@Tag(name = "01. SaleTax", description = "SaleTax Controller")
public class SaleTaxController {

    private final SaleRepository saleRepository;

    private final SaleTaxService saleTaxService;

    private final BusinessRepository businessRepository;

    @ValidateRoles({ "business" })
    @PostMapping(SaleTaxStatics.Endpoint.SAVE)
    @Operation(summary = "Create new Sale tax", description = "Create a new Sale tax")
    public ResponseEntity<TaxDTO> save(@RequestBody SaleTaxDTO dto)
            throws NoContentException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
            return ResponseEntity.ok(saleTaxService.save(dto));
        } else {
            Long businessId = businessRepository.findByUserEmail(auth.getName()).get().getId();
            Long saleId = dto.getSaleId();
            // Check if the payment option is from the same sale
            if (!saleRepository.existsByIdAndBranch_Business_Id(saleId, businessId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }
            return ResponseEntity.ok(saleTaxService.save(dto));
        }
    }

    @ValidateSaleTaxOwner
    @ValidateRoles({ "business" })
    @PutMapping(SaleTaxStatics.Endpoint.UPDATE)
    @Operation(summary = "Update Defaulttax", description = "Updates the data of a Defaulttax given its ID and DTO")
    public ResponseEntity<TaxDTO> update(
            @PathVariable("id") Long id,
            @RequestBody TaxDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(saleTaxService.update(id, dto));
    }

    @ValidateSaleTaxOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(SaleTaxStatics.Endpoint.DELETE)
    @Operation(summary = "Delete Defaulttax", description = "Delete the data of a Defaulttax given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        saleTaxService.delete(id);
    }
}