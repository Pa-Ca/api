package com.paca.paca.business.controller;

import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.branch.dto.BranchInfoListDTO;
import com.paca.paca.business.statics.BusinessStatics;
import com.paca.paca.business.service.BusinessService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.business.utils.ValidateBusinessInterceptor.ValidateBusiness;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(BusinessStatics.Endpoint.PATH)
@Tag(name = "04. Business", description = "Business Management Controller")
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping(BusinessStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get business by ID", description = "Gets the data of a business given its ID")
    public ResponseEntity<BusinessDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(businessService.getById(id));
    }

    @ValidateRoles({ "business" })
    @PostMapping(BusinessStatics.Endpoint.SAVE)
    @Operation(summary = "Create new business", description = "Register a new business in the app")
    public ResponseEntity<BusinessDTO> save(@RequestBody BusinessDTO business)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(businessService.save(business));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @PutMapping(BusinessStatics.Endpoint.UPDATE)
    @Operation(summary = "Update business", description = "Updates the data of a business given its ID")
    public ResponseEntity<BusinessDTO> update(@PathVariable("id") Long id, @RequestBody BusinessDTO business)
            throws NoContentException {
        return ResponseEntity.ok(businessService.update(id, business));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @DeleteMapping(BusinessStatics.Endpoint.DELETE)
    @Operation(summary = "Delete business", description = "Delete the data of a business given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        businessService.delete(id);
    }

    @GetMapping(BusinessStatics.Endpoint.GET_BY_USER_ID)
    @Operation(summary = "Get business by user ID", description = "Gets the data of a business given its user ID")
    public ResponseEntity<BusinessDTO> getByUserId(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(businessService.getByUserId(id));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @GetMapping(BusinessStatics.Endpoint.GET_BRANCHES)
    @Operation(summary = "Get business branches", description = "Gets all the branches of a business given its ID")
    public ResponseEntity<BranchInfoListDTO> getAllBranches(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(businessService.getAllBranchesById(id));
    }
}