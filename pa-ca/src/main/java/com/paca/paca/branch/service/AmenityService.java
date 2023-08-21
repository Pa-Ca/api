package com.paca.paca.branch.service;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.branch.utils.AmenityMapper;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.branch.repository.BranchAmenityRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmenityService {

    private final BranchMapper branchMapper;

    private final AmenityMapper amenityMapper;

    private final BranchRepository branchRepository;

    private final AmenityRepository amenityRepository;

    private final BranchAmenityRepository branchAmenityRepository;

    private AmenityListDTO getAmenitiesByBranch(Optional<Branch> branch) {
        List<AmenityDTO> response = new ArrayList<>();
        branchAmenityRepository.findAllByBranchId(branch.get().getId()).forEach(
                branchAmenity -> response.add(AmenityDTO
                        .builder()
                        .id(branchAmenity.getAmenity().getId())
                        .name(branchAmenity.getAmenity().getName())
                        .build()));

        return AmenityListDTO.builder().amenities(response).build();
    }

    public AmenityListDTO getAll() {
        List<AmenityDTO> response = new ArrayList<>();
        amenityRepository.findAll().forEach(amenity -> response.add(
                AmenityDTO
                        .builder()
                        .id(amenity.getId())
                        .name(amenity.getName())
                        .build()));

        return AmenityListDTO.builder().amenities(response).build();
    }

    public AmenityListDTO getBySearchWord(String word) {
        List<AmenityDTO> response = new ArrayList<>();
        amenityRepository.findAllMatching(word).forEach(amenity -> response.add(
                AmenityDTO
                        .builder()
                        .id(amenity.getId())
                        .name(amenity.getName())
                        .build()));

        return AmenityListDTO.builder().amenities(response).build();
    }

    public AmenityListDTO getAllByBranchId(Long id) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch does not exists", 12);
        }

        return getAmenitiesByBranch(branch);
    }

    public AmenityListDTO saveAllByBranchId(Long id, AmenityListDTO dto) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty())
            throw new NoContentException("Branch does not exists", 12);

        dto.getAmenities().forEach(amenityDTO -> {
            Long amenityId = amenityMapper.toEntity(amenityDTO).getId();
            Optional<Amenity> amenity = amenityRepository.findById(amenityId);

            if (amenity.isEmpty()) {
                throw new NoContentException("Amenity does not exists", 20);
            }
        });

        dto.getAmenities().forEach(amenityDTO -> {
            Long amenityId = amenityMapper.toEntity(amenityDTO).getId();
            Optional<Amenity> amenity = amenityRepository.findById(amenityId);
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

        return getAmenitiesByBranch(branch);
    }

    public AmenityListDTO deleteAllByBranchId(Long id, AmenityListDTO dto)
            throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(id);

        if (branch.isEmpty()) {
            throw new NoContentException("Branch does not exists", 12);
        }

        dto.getAmenities().forEach(amenityDTO -> {
            Long amenityId = amenityMapper.toEntity(amenityDTO).getId();
            Optional<Amenity> amenity = amenityRepository.findById(amenityId);

            if (amenity.isEmpty()) {
                throw new NoContentException("Amenity does not exists", 20);
            }
        });

        dto.getAmenities().forEach(amenityDTO -> {
            Long amenityId = amenityMapper.toEntity(amenityDTO).getId();
            Optional<BranchAmenity> branchAmenity = branchAmenityRepository
                    .findByBranchIdAndAmenityId(id, amenityId);

            branchAmenity.ifPresent(branchAmenityRepository::delete);
        });

        return getAmenitiesByBranch(branch);
    }

    public BranchListDTO getAllBranches(Long id) throws NoContentException {
        if (!amenityRepository.existsById(id)) {
            throw new NoContentException("Amenity with id " + id + " does not exists", 34);
        }

        List<BranchDTO> response = new ArrayList<>();
        branchAmenityRepository.findAllByAmenityId(id).forEach(branchAmenity -> {
            BranchDTO dto = branchMapper.toDTO(branchAmenity.getBranch());
            response.add(dto);
        });

        return BranchListDTO.builder().branches(response).build();
    }
}
