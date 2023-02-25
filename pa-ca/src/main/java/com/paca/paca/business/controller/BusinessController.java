package com.paca.paca.business.controller;

import com.paca.paca.business.dto.BusinessListDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
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

    @GetMapping
    public ResponseEntity<BusinessListDTO> getAll() {
        return ResponseEntity.ok(businessService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(businessService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BusinessDTO> save(@RequestBody BusinessDTO business)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(businessService.save(business));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessDTO> update(@PathVariable("id") Long id, @RequestBody BusinessDTO business)
            throws NoContentException {
        return ResponseEntity.ok(businessService.update(id, business));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        businessService.delete(id);
    }

}