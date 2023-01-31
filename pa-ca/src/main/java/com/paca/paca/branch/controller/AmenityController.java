package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.branch.statics.AmenityStatics;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(AmenityStatics.Endpoint.AMENITY_PATH)
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController (AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<AmenityListDTO> getAll () { return amenityService.getAll(); }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_SEARCH_PATH)
    public ResponseEntity<AmenityListDTO> getBySearchTerm (@PathVariable("term") String term) {
        return amenityService.getBySearchTerm(term);
    }
}
