package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.branch.statics.AmenityStatics;
import com.paca.paca.exception.exceptions.NoContentException;

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
@RequestMapping(AmenityStatics.Endpoint.AMENITY_PATH)
@Tag(name = "09. Amenity", description = "Amenity Management Controller")
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    @Operation(summary = "Get all amenities", description = "Returns a list with all amenities")
    public ResponseEntity<AmenityListDTO> getAll() {
        return ResponseEntity.ok(amenityService.getAll());
    }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_SEARCH_PATH)
    @Operation(summary = "Get all amenities by word", description = "Gets all amenities whose names are syntactically similar to a word")
    public ResponseEntity<AmenityListDTO> getBySearchWord(@PathVariable("word") String word) {
        return ResponseEntity.ok(amenityService.getBySearchWord(word));
    }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_BRANCHES)
    @Operation(summary = "Gets all branches associated with an amenity", description = "Gets the data of all the branches associated with an amenity given its ID")
    public ResponseEntity<BranchListDTO> getAllBranches(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(amenityService.getAllBranches(id));
    }
}
