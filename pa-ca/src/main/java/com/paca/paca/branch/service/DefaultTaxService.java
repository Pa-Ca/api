package com.paca.paca.branch.service;

import java.util.Optional;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.repository.DefaultTaxRepository;
// import the mapper
import com.paca.paca.branch.utils.DefaultTaxMapper;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
// import the DTO's
import com.paca.paca.branch.dto.DefaultTaxDTO;
// Import the statics
import com.paca.paca.branch.statics.DefaultTaxStatics;

// Import branch and the branch repository
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;


@Service
@RequiredArgsConstructor
public class DefaultTaxService {

    private final BranchRepository branchRepository;
    private final DefaultTaxMapper defaultTaxMapper;
    private final DefaultTaxRepository defaultTaxRepository;

    public DefaultTaxDTO save(DefaultTaxDTO defaultTaxDTO) throws NoContentException{
        // Check if the branch exists
        long branchId = defaultTaxDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
                    throw new NoContentException(
                            "Branch with id " + branchId + " does not exists",
                            20);
                }
        // Create a default tax
        DefaultTax defaultTax = defaultTaxMapper.toEntity(defaultTaxDTO, branch.get());
        // Save the default tax
        defaultTax = defaultTaxRepository.save(defaultTax);
        // Return the default tax
        return defaultTaxMapper.toDTO(defaultTax);
    }

    public DefaultTaxDTO update(DefaultTaxDTO defaultTaxDTO) throws NoContentException, BadRequestException{

        // Check if the default tax exists
        long defaultTaxId = defaultTaxDTO.getId();
        Optional<DefaultTax> defaultTax = defaultTaxRepository.findById(defaultTaxId);
        if (defaultTax.isEmpty()) {
                    throw new NoContentException(
                            "Default tax with id " + defaultTaxId + " does not exists",
                            50);
                }
        // Check that the defautl tax type belongs to the enum from the statics
        Integer defaultTaxType = defaultTaxDTO.getType();
                if (!DefaultTaxStatics.Types.isTypeValid(defaultTaxType)) {
            throw new BadRequestException(
                    "Invalid default tax type:" + defaultTaxType,
                    51);
        }
        
        // Check if the branch exists (This may be redundant)
        long branchId = defaultTaxDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
                    throw new NoContentException(
                            "Branch with id " + branchId + " does not exists",
                            20);
                }
        
        // Update the default tax
        DefaultTax defaultTaxToUpdate = defaultTax.get();
        defaultTaxToUpdate = defaultTaxMapper.updateModel(defaultTaxDTO, defaultTaxToUpdate);

        // Save the default tax
        defaultTaxToUpdate = defaultTaxRepository.save(defaultTaxToUpdate);

        // Return the default tax
        return defaultTaxMapper.toDTO(defaultTaxToUpdate);

    }

    public void delete(Long defaultTaxId) throws NoContentException{
        // Check if the default tax exists
        Optional<DefaultTax> defaultTax = defaultTaxRepository.findById(defaultTaxId);
        if (defaultTax.isEmpty()) {
                    throw new NoContentException(
                            "Default tax with id " + defaultTaxId + " does not exists",
                            50);
                }
        // Delete the default tax
        defaultTaxRepository.deleteById(defaultTaxId);
    }
        
}
