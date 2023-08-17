package com.paca.paca.branch.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.branch.statics.DefaultTaxStatics;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;

@Service
@RequiredArgsConstructor
public class DefaultTaxService {

    private final TaxMapper taxMapper;

    private final TaxRepository taxRepository;

    private final BranchRepository branchRepository;

    private final DefaultTaxRepository defaultTaxRepository;

    public TaxDTO save(DefaultTaxDTO defaultTaxDTO) throws NoContentException {
        long branchId = defaultTaxDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        TaxDTO taxDTO = defaultTaxDTO.getTax();
        Short taxType = taxDTO.getType();
        if (!DefaultTaxStatics.Types.isTypeValid(taxType)) {
            throw new BadRequestException(
                    "Invalid tax type: " + taxType,
                    51);
        }

        Tax tax = taxMapper.toEntity(taxDTO);

        tax = taxRepository.save(tax);
        defaultTaxRepository.save(DefaultTax.builder()
                .branch(branch.get())
                .tax(tax)
                .build());

        return taxMapper.toDTO(tax);
    }

    public TaxDTO update(long id, TaxDTO dto)
            throws NoContentException, BadRequestException {

        // Check if the default tax exists
        Optional<Tax> tax = taxRepository.findById(id);
        if (tax.isEmpty()) {
            throw new NoContentException(
                    "Tax with id " + id + " does not exists",
                    52);
        }
        // Check that the defautl tax type belongs to the enum from the statics
        Short taxType = dto.getType();
        if (taxType != null && !DefaultTaxStatics.Types.isTypeValid(taxType)) {
            throw new BadRequestException(
                    "Invalid tax type:" + taxType,
                    51);
        }

        // Update the default tax
        Tax taxToUpdate = tax.get();
        taxToUpdate = taxMapper.updateModel(dto, taxToUpdate);
        taxToUpdate = taxRepository.save(taxToUpdate);

        // Return the default tax
        return taxMapper.toDTO(taxToUpdate);
    }

    public void delete(Long taxId) throws NoContentException {
        // Check if the default tax exists
        Optional<Tax> tax = taxRepository.findById(taxId);
        if (tax.isEmpty()) {
            throw new NoContentException(
                    "Tax with id " + taxId + " does not exists",
                    52);
        }
        // Delete the default tax
        taxRepository.deleteById(taxId);
    }

}
