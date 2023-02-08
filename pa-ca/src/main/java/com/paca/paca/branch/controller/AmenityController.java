package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.branch.statics.AmenityStatics;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.exception.exceptions.NoContentException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(BranchStatics.Endpoint.PATH)
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping(AmenityStatics.Endpoint.AMENITY_PATH)
    public ResponseEntity<AmenityListDTO> getAll() {
        return amenityService.getAll();
    }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_SEARCH_PATH)
    public ResponseEntity<AmenityListDTO> getBySearchWord(@PathVariable("word") String word) {
        return amenityService.getBySearchWord(word);
    }

    @GetMapping(AmenityStatics.Endpoint.AMENITY_FROM_BRANCH_ID_PATH)
    public ResponseEntity<AmenityListDTO> getAllByBranchId(@PathVariable("id") Long id) throws NoContentException {
        return amenityService.getAllByBranchId(id);
    }

    @PostMapping(AmenityStatics.Endpoint.AMENITY_FROM_BRANCH_ID_PATH)
    public ResponseEntity<AmenityListDTO> saveBranchAmenities(
            @PathVariable("id") Long id, @RequestBody AmenityListDTO dto) throws NoContentException {
        return amenityService.saveAllByBranchId(id, dto);
    }

    @DeleteMapping(AmenityStatics.Endpoint.AMENITY_FROM_BRANCH_ID_PATH)
    public ResponseEntity<AmenityListDTO> deleteAllByBranchId(
            @PathVariable("id") Long id,
            @RequestBody AmenityListDTO dto) throws NoContentException {
        return amenityService.deleteAllByBranchId(id, dto);
    }
}
