package com.paca.paca.sale.controller;

import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;

import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.service.TaxService;
import com.paca.paca.sale.dto.TaxDTO;



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
@RequestMapping(TaxStatics.Endpoint.PATH)
@Tag(name = "01. Tax", description = "Tax Controller")
public class TaxController {

    private final TaxService taxService;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new tax", description = "Create a new tax")
    public ResponseEntity<TaxDTO> save(@RequestBody TaxDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(taxService.save(dto));
    }
    

    @PutMapping("/{id}") // Hacer otro interceptor para validar que el usuario sea el dueño del negocio
    @ValidateRoles({ "business" })
    @Operation(summary = "Update tax", description = "Updates the data of a tax given its ID and DTO")
    public ResponseEntity<TaxDTO> update(
            @PathVariable("id") Long id,
            @RequestBody TaxDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(taxService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @Operation(summary = "Delete tax", description = "Delete the data of a tax given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        taxService.delete(id);
    }
}
