package com.paca.paca.branch.service;

import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.repository.AmenityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public ResponseEntity<AmenityListDTO> getAll() {
        List<AmenityDTO> response = new ArrayList<>();
        amenityRepository.findAll().forEach(amenity -> response.add (
                AmenityDTO
                        .builder()
                        .id(amenity.getId())
                        .name(amenity.getName())
                        .build()
        ));

        return ResponseEntity.ok(AmenityListDTO.builder().amenities(response).build());
    }

    public ResponseEntity<AmenityListDTO> getBySearchTerm(String term) {
        List<AmenityDTO> response = new ArrayList<>();
        amenityRepository.findAllMatching(term).forEach(amenity -> response.add (
                AmenityDTO
                        .builder()
                        .id(amenity.getId())
                        .name(amenity.getName())
                        .build()
        ));

        return ResponseEntity.ok(AmenityListDTO.builder().amenities(response).build());
    }
}
