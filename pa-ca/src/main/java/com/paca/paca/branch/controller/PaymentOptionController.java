package com.paca.paca.branch.controller;


import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.branch.utils.ValidatePaymentOptionOwnerInterceptor.ValidatePaymentOptionOwner;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;



import com.paca.paca.branch.statics.PaymentOptionStatics;
import com.paca.paca.branch.service.PaymentOptionService;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.branch.repository.BranchRepository;

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
@RequestMapping(PaymentOptionStatics.Endpoint.PATH)
@Tag(name = "01. PaymentOption", description = "Payment option Controller")
public class PaymentOptionController {

    private final PaymentOptionService paymentOptionService;
    private final BusinessRepository businessRepository;
    private final BranchRepository branchRepository;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new payment option", description = "Create a new payment option")
    public ResponseEntity<PaymentOptionDTO> save(@RequestBody PaymentOptionDTO dto)
            throws NoContentException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
            return ResponseEntity.ok(paymentOptionService.save(dto));
        }
        else{
            Long businessId = businessRepository.findByUserEmail(auth.getName()).get().getId();
            Long branchId = dto.getBranchId();
            // Check if the payment option is from the same branch
            if (!branchRepository.existsByIdAndBusinessId(branchId, businessId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }             
            return ResponseEntity.ok(paymentOptionService.save(dto));
        }
    }
    

    @PutMapping("/{id}") // Hacer otro interceptor para validar que el usuario sea el dueño del negocio
    @ValidateRoles({ "business" })
    @ValidatePaymentOptionOwner
    @Operation(summary = "Update a payment option", description = "Updates the data of a payment option given its ID and DTO")
    public ResponseEntity<PaymentOptionDTO> update(
            @PathVariable("id") Long id,
            @RequestBody PaymentOptionDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(paymentOptionService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidatePaymentOptionOwner
    @Operation(summary = "Delete a payment option", description = "Delete the data of a payment option given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        paymentOptionService.delete(id);
    }
}
