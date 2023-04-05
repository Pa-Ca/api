package com.paca.paca.business.controller;

import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.business.dto.BusinessListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.business.utils.ValidateBusinessInterceptor.ValidateBusiness;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paca.paca.business.service.BusinessService;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @ValidateRoles({})
    @GetMapping
    public ResponseEntity<BusinessListDTO> getAll() {
        return ResponseEntity.ok(businessService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(businessService.getById(id));
    }

    @ValidateRoles({ "business" })
    @PostMapping
    public ResponseEntity<BusinessDTO> save(@RequestBody BusinessDTO business)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(businessService.save(business));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @PutMapping("/{id}")
    public ResponseEntity<BusinessDTO> update(@PathVariable("id") Long id, @RequestBody BusinessDTO business)
            throws NoContentException {
        return ResponseEntity.ok(businessService.update(id, business));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        businessService.delete(id);
    }

}