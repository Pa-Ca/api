package com.paca.paca.branch.controller;


import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;

import com.paca.paca.branch.statics.DefaultTaxStatics;
import com.paca.paca.branch.service.DefaultTaxService;
import com.paca.paca.branch.dto.DefaultTaxDTO;



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
@RequestMapping(DefaultTaxStatics.Endpoint.PATH)
@Tag(name = "01. DefaultTax", description = "DefaultTax Controller")
public class DefaultTaxController {

    private final DefaultTaxService defaultTaxService;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new Defaulttax", description = "Create a new Defaulttax")
    public ResponseEntity<DefaultTaxDTO> save(@RequestBody DefaultTaxDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(defaultTaxService.save(dto));
    }
    

    @PutMapping("/{id}") // Hacer otro interceptor para validar que el usuario sea el dueño del negocio
    @ValidateRoles({ "business" })
    @Operation(summary = "Update Defaulttax", description = "Updates the data of a Defaulttax given its ID and DTO")
    public ResponseEntity<DefaultTaxDTO> update(
            @PathVariable("id") Long id,
            @RequestBody DefaultTaxDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(defaultTaxService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @Operation(summary = "Delete Defaulttax", description = "Delete the data of a Defaulttax given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        defaultTaxService.delete(id);
    }
}
