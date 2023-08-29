package com.paca.paca.sale.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleTax;
import com.paca.paca.sale.dto.SaleTaxDTO;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.SaleTaxRepository;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.exception.exceptions.BadRequestException;

@Service
@RequiredArgsConstructor
public class SaleTaxService {

    private final TaxMapper taxMapper;

    private final TaxRepository taxRepository;

    private final SaleRepository saleRepository;

    private final SaleTaxRepository saleTaxRepository;

    public TaxDTO save(SaleTaxDTO saleTaxDTO) throws NotFoundException {
        long saleId = saleTaxDTO.getSaleId();
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
            throw new NotFoundException(
                    "Sale with id " + saleId + " does not exists",
                    42);
        }

        TaxDTO taxDTO = saleTaxDTO.getTax();
        Short taxType = taxDTO.getType();
        if (!TaxStatics.Types.isTypeValid(taxType)) {
            throw new BadRequestException(
                    "Invalid tax type: " + taxType,
                    51);
        }

        Tax tax = taxMapper.toEntity(taxDTO);

        tax = taxRepository.save(tax);
        saleTaxRepository.save(SaleTax.builder()
                .sale(sale.get())
                .tax(tax)
                .build());

        return taxMapper.toDTO(tax);
    }

    public TaxDTO update(long id, TaxDTO dto)
            throws NotFoundException, BadRequestException {

        // Check if the default tax exists
        Optional<Tax> tax = taxRepository.findById(id);
        if (tax.isEmpty()) {
            throw new NotFoundException(
                    "Tax with id " + id + " does not exists",
                    52);
        }
        // Check that the defautl tax type belongs to the enum from the statics
        Short taxType = dto.getType();
        if (taxType != null && !TaxStatics.Types.isTypeValid(taxType)) {
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

    public void delete(Long taxId) throws NotFoundException {
        // Check if the default tax exists
        Optional<Tax> tax = taxRepository.findById(taxId);
        if (tax.isEmpty()) {
            throw new NotFoundException(
                    "Tax with id " + taxId + " does not exists",
                    52);
        }
        // Delete the default tax
        taxRepository.deleteById(taxId);
    }
}
