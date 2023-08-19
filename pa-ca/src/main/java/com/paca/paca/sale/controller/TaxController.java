package com.paca.paca.sale.controller;

import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.service.TaxService;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.sale.utils.ValidateTaxOwnerInterceptor.ValidateTaxOwner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(TaxStatics.Endpoint.PATH)
@Tag(name = "01. Tax", description = "Tax Controller")
public class TaxController {

    private final TaxService taxService;

    @ValidateTaxOwner
    @ValidateRoles({ "business" })
    @PutMapping(TaxStatics.Endpoint.UPDATE)
    @Operation(summary = "Update tax", description = "Updates the data of a tax given its ID and DTO")
    public ResponseEntity<TaxDTO> update(
            @PathVariable("id") Long id,
            @RequestBody TaxDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(taxService.update(id, dto));
    }

    @ValidateTaxOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(TaxStatics.Endpoint.DELETE)
    @Operation(summary = "Delete tax", description = "Delete the data of a tax given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        taxService.delete(id);
    }
}
