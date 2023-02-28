package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.branch.statics.AmenityStatics;
import com.paca.paca.exception.exceptions.NoContentException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(AmenityStatics.Endpoint.AMENITY_PATH)
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    public ResponseEntity<AmenityListDTO> getAll() {
        return ResponseEntity.ok(amenityService.getAll());
    }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_SEARCH_PATH)
    public ResponseEntity<AmenityListDTO> getBySearchWord(@PathVariable("word") String word) {
        return ResponseEntity.ok(amenityService.getBySearchWord(word));
    }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_BRANCHES)
    public ResponseEntity<BranchListDTO> getAllBranches(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(amenityService.getAllBranches(id));
    }
}
