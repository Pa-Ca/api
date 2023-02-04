package com.paca.paca.branch.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;

public class BranchService {

    private final BranchMapper branchMapper;

    private final BranchRepository branchRepository;

    private final BusinessRepository businessRepository;

    public BranchService(
            BranchRepository branchRepository,
            BranchMapper branchMapper,
            BusinessRepository businessRepository) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.businessRepository = businessRepository;
    }

    public ResponseEntity<BranchListDTO> getAll() {
        List<BranchDTO> response = new ArrayList<>();
        branchRepository.findAll().forEach(branch -> {
            BranchDTO dto = branchMapper.toDTO(branch);
            dto.setBusinessId(branch.getBusiness().getId());
            response.add(dto);
        });

        return ResponseEntity.ok(BranchListDTO.builder().branches(response).build());
    }

    public ResponseEntity<BranchDTO> getById(Long id) throws NoContentException {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Branch with id " + id + " does not exists",
                        20));

        BranchDTO dto = branchMapper.toDTO(branch);
        dto.setBusinessId(branch.getBusiness().getId());
        return new ResponseEntity<BranchDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<BranchDTO> save(BranchDTO branchDto) throws NoContentException, BadRequestException {
        Optional<Business> business = businessRepository.findById(branchDto.getBusinessId());
        if (business.isEmpty()) {
            throw new NoContentException(
                    "Business with id " + branchDto.getBusinessId() + " does not exists",
                    21);
        }
        if (branchDto.getCapacity() < 1) {
            throw new BadRequestException(
                    "Branch capacity must be greater than 0",
                    22);
        }

        Branch newBranch = branchRepository.save(branchMapper.toEntity(branchDto));
        newBranch.setBusiness(business.get());

        BranchDTO newDto = branchMapper.toDTO(newBranch);
        newDto.setBusinessId(newBranch.getBusiness().getId());

        return new ResponseEntity<BranchDTO>(newDto, HttpStatus.OK);
    }

    public ResponseEntity<BranchDTO> update(Long id, BranchDTO branchDto)
            throws NoContentException, BadRequestException {
        Optional<Branch> current = branchRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }

        if (branchDto.getCapacity() < 1) {
            throw new BadRequestException(
                    "Branch capacity must be greater than 0",
                    21);
        }

        Branch newBranch = branchMapper.updateModel(current.get(), branchDto);
        BranchDTO newDto = branchMapper.toDTO(newBranch);
        newDto.setBusinessId(newBranch.getBusiness().getId());

        return new ResponseEntity<BranchDTO>(newDto, HttpStatus.OK);
    }

    public void delete(Long id) throws NoContentException {
        Optional<Branch> current = branchRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Branch with id: " + id + " does not exists",
                    20);
        }
        branchRepository.deleteById(id);
    }

}
