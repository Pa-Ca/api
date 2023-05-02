package com.paca.paca.business.controller;

import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.business.dto.BusinessListDTO;
import com.paca.paca.business.statics.BusinessStatics;
import com.paca.paca.business.service.BusinessService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.business.utils.ValidateBusinessInterceptor.ValidateBusiness;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(BusinessStatics.Endpoint.PATH)
public class BusinessController {

    private final BusinessService businessService;

    @ValidateRoles({})
    @GetMapping(BusinessStatics.Endpoint.GET_ALL)
    public ResponseEntity<BusinessListDTO> getAll() {
        return ResponseEntity.ok(businessService.getAll());
    }

    @GetMapping(BusinessStatics.Endpoint.GET_BY_ID)
    public ResponseEntity<BusinessDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(businessService.getById(id));
    }

    @ValidateRoles({ "business" })
    @PostMapping(BusinessStatics.Endpoint.SAVE)
    public ResponseEntity<BusinessDTO> save(@RequestBody BusinessDTO business)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(businessService.save(business));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @PutMapping(BusinessStatics.Endpoint.UPDATE)
    public ResponseEntity<BusinessDTO> update(@PathVariable("id") Long id, @RequestBody BusinessDTO business)
            throws NoContentException {
        return ResponseEntity.ok(businessService.update(id, business));
    }

    @ValidateBusiness
    @ValidateRoles({ "business" })
    @DeleteMapping(BusinessStatics.Endpoint.DELETE)
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        businessService.delete(id);
    }

    @GetMapping(BusinessStatics.Endpoint.GET_BY_USER_ID)
    public ResponseEntity<BusinessDTO> getByUserId(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(businessService.getByUserId(id));
    }
}