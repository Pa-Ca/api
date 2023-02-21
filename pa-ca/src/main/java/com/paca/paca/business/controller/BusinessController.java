package com.paca.paca.business.controller;

import com.paca.paca.business.dto.BusinessListDTO;
import com.paca.paca.business.dto.BusinessDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}