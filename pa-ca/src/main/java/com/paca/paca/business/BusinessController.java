package com.paca.paca.business;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {
    
    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    public List<Business> getAll() {
        return businessService.getAll();
    }

    @GetMapping("/{id}")
    public Business getUserById(@PathVariable("id") Long id) {
        return businessService.getBusinessById(id);
    }
}