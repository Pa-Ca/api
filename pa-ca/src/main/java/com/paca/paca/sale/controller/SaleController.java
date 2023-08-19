package com.paca.paca.sale.controller;

import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.sale.utils.ValidateSaleOwnerInterceptor.ValidateSaleOwner;

import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.reservation.repository.ReservationRepository;

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

    private final BranchRepository branchRepository;

    private final BusinessRepository businessRepository;

    private final ReservationRepository reservationRepository;

    @ValidateRoles({ "business" })
    @PostMapping(SaleStatics.Endpoint.SAVE)
    @Operation(summary = "Create new sale", description = "Create a new sale")
    public ResponseEntity<SaleInfoDTO> save(@RequestBody SaleInfoDTO dto)
            throws NoContentException, BadRequestException, ForbiddenException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
            return ResponseEntity.ok(saleService.save(dto));
        } else {
            Long businessId = businessRepository.findByUserEmail(auth.getName()).get().getId();
            Long branchId = dto.getSale().getBranchId();
            Long reservationId = dto.getReservationId();

            if (!branchRepository.existsByIdAndBusinessId(branchId, businessId)) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }

            if (reservationId != null) {
                // Check if the reservation is from the same branch
                if (!reservationRepository.existsByIdAndBranch_Business_Id(reservationId, businessId)) {
                    throw new ForbiddenException("Unauthorized access for this operation");
                }
            }

            return ResponseEntity.ok(saleService.save(dto));
        }

    }

    @ValidateSaleOwner
    @ValidateRoles({ "business" })
    @PutMapping(SaleStatics.Endpoint.UPDATE)
    @Operation(summary = "Update sale", description = "Updates the data of a sale given its ID and DTO")
    public ResponseEntity<SaleInfoDTO> update(
            @PathVariable("id") Long id,
            @RequestBody SaleDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(saleService.update(id, dto));
    }

    @ValidateSaleOwner
    @ValidateRoles({ "business" })
    @PostMapping(SaleStatics.Endpoint.ADD_TAX)
    @Operation(summary = "Create new tax", description = "Create a new tax")
    public ResponseEntity<TaxDTO> addTax(@PathVariable("id") Long id, @RequestBody TaxDTO dto)
            throws NoContentException, ForbiddenException {
        return ResponseEntity.ok(saleService.addTax(id, dto));
    }

    @ValidateSaleOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(SaleStatics.Endpoint.DELETE)
    @Operation(summary = "Delete table", description = "Delete the data of a table given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        saleService.delete(id);
    }

    @Transactional
    @ValidateSaleOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(SaleStatics.Endpoint.CLEAR)
    @Operation(summary = "Delete sale products form a sale", description = "Deletes all the sales products of a sale givent the slae ID")
    public void clearSaleProducts(@PathVariable("id") Long id) throws NoContentException, BadRequestException {
        saleService.clearSaleProducts(id);
    }

}
