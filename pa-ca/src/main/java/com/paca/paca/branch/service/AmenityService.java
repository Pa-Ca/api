package com.paca.paca.branch.service;

import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.utils.AmenityMapper;
import com.paca.paca.exception.exceptions.NoContentException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {
    private final BranchRepository branchRepository;
    private final BranchAmenityRepository branchAmenityRepository;
    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    private ResponseEntity<AmenityListDTO> getAmenityListDTOByBranch(Optional<Branch> branch) {
        List<AmenityDTO> response = new ArrayList<>();
        branchAmenityRepository.findAllByBranchId(branch.get().getId()).forEach(branchAmenity -> response.add(
                AmenityDTO
                        .builder()
                        .id(branchAmenity.getAmenity().getId())
                        .name(branchAmenity.getAmenity().getName())
                        .build()));

        return ResponseEntity.ok(AmenityListDTO.builder().amenities(response).build());
    }

    public AmenityService(
            BranchRepository branchRepository,
            BranchAmenityRepository branchAmenityRepository,
            AmenityRepository amenityRepository,
            AmenityMapper amenityMapper) {
        this.branchRepository = branchRepository;
        this.branchAmenityRepository = branchAmenityRepository;
        this.amenityRepository = amenityRepository;
        this.amenityMapper = amenityMapper;
    }

    public ResponseEntity<AmenityListDTO> getAll() {
        List<AmenityDTO> response = new ArrayList<>();
        amenityRepository.findAll().forEach(amenity -> response.add(
                AmenityDTO
                        .builder()
                        .id(amenity.getId())
                        .name(amenity.getName())
                        .build()));

        return ResponseEntity.ok(AmenityListDTO.builder().amenities(response).build());
    }

    public ResponseEntity<AmenityListDTO> getBySearchWord(String word) {
        List<AmenityDTO> response = new ArrayList<>();
        amenityRepository.findAllMatching(word).forEach(amenity -> response.add(
                AmenityDTO
                        .builder()
                        .id(amenity.getId())
                        .name(amenity.getName())
                        .build()));

        return ResponseEntity.ok(AmenityListDTO.builder().amenities(response).build());
    }

    public ResponseEntity<AmenityListDTO> getAllByBranchId(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty())
            throw new NoContentException("Branch does not exists", 12);

        return getAmenityListDTOByBranch(branch);
    }

    public ResponseEntity<AmenityListDTO> saveAllByBranchId(Long id, AmenityListDTO dto) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty())
            throw new NoContentException("Branch does not exists", 12);

        dto.getAmenities().forEach(amenityDTO -> {
            Long amenityId = amenityMapper.toEntity(amenityDTO).getId();
            Optional<Amenity> amenity = amenityRepository.findById(amenityId);
            if (amenity.isEmpty())
                throw new NoContentException("Amenity does not exists", 20);

            Optional<BranchAmenity> branchAmenityCheck = branchAmenityRepository
                    .findByBranchIdAndAmenityId(id, amenityId);
            if (branchAmenityCheck.isEmpty()) {
                BranchAmenity branchAmenity = BranchAmenity
                        .builder()
                        .branch(branch.get())
                        .amenity(amenity.get())
                        .build();
                branchAmenityRepository.save(branchAmenity);
            }
        });

        return getAmenityListDTOByBranch(branch);
    }

    public ResponseEntity<AmenityListDTO> deleteAllByBranchId(Long id, AmenityListDTO dto)
            throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty())
            throw new NoContentException("Branch does not exists", 12);

        dto.getAmenities().forEach(amenityDTO -> {
            Long amenityId = amenityMapper.toEntity(amenityDTO).getId();
            Optional<Amenity> amenity = amenityRepository.findById(amenityId);
            if (amenity.isEmpty())
                throw new NoContentException("Amenity does not exists", 20);

            Optional<BranchAmenity> branchAmenity = branchAmenityRepository.findByBranchIdAndAmenityId(id, amenityId);
            branchAmenity.ifPresent(branchAmenityRepository::delete);
        });

        return getAmenityListDTOByBranch(branch);
    }
}
