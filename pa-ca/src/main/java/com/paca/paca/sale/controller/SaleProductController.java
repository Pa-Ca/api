package com.paca.paca.sale.controller;

import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.sale.utils.ValidateSaleProductOwnerInterceptor.ValidateSaleProductOwner;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.sale.statics.SaleProductStatics;
import com.paca.paca.sale.service.SaleProductService;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.repository.SaleRepository;

//import BigDecimal


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final BusinessRepository businessRepository;
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new sale product", description = "Create a new SaleProduct")
    public ResponseEntity<SaleProductDTO> save(@RequestBody SaleProductDTO dto)
            throws NoContentException {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
            return ResponseEntity.ok(saleProductService.save(dto));
        }
        else{
            Long businessId = businessRepository.findByUserEmail(auth.getName()).get().getId();
            Long saleId = dto.getSaleId();
            Long productId = dto.getProductId();
            if (!saleRepository.existsByIdAndTable_Branch_Business_Id(saleId, businessId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }
            // Check if the reservation is from the same branch
            if (!productRepository.existsByIdAndSubCategory_Branch_Business_Id(productId, businessId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }  
            return ResponseEntity.ok(saleProductService.save(dto));
        }
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
