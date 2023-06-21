package com.paca.paca.sale.service;

import java.util.List;
import java.util.Optional;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.repository.TaxRepository;
// import the mapper
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
// import the DTO's
import com.paca.paca.sale.dto.TaxDTO;
// Import the statics
import com.paca.paca.sale.statics.TaxStatics;


// Import the sales repository
import com.paca.paca.sale.repository.SaleRepository;



@Service
@RequiredArgsConstructor
public class TaxService {

    private final SaleRepository saleRepository;
    private final TaxMapper TaxMapper;
    private final TaxRepository TaxRepository;


    // Create a method that saves a  tax (from the DTO)
    public TaxDTO save(TaxDTO TaxDTO) throws NoContentException{
        // Check if the branch exists
        long saleId = TaxDTO.getSaleId();
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
                    throw new NoContentException(
                            "Sale with id " + saleId + " does not exists",
                            42);
                }
        // Create a  tax
        Tax Tax = TaxMapper.toEntity(TaxDTO, sale.get());
        // Save the  tax
        Tax = TaxRepository.save(Tax);
        // Return the  tax
        return TaxMapper.toDTO(Tax);
    }

    // Create a method that updates a  tax (from the DTO)
    public TaxDTO update(TaxDTO TaxDTO) throws NoContentException, BadRequestException{

        // Check if the  tax exists
        long TaxId = TaxDTO.getId();
        Optional<Tax> Tax = TaxRepository.findById(TaxId);
        if (Tax.isEmpty()) {
                    throw new NoContentException(
                            "Tax with id " + TaxId + " does not exists",
                            52);
                }
        // Check that the defautl tax type belongs to the enum from the statics
        Integer TaxType = TaxDTO.getType();
                if (!TaxStatics.Types.isTypeValid(TaxType)) {
            throw new BadRequestException(
                    "Invalid tax type:" + TaxType,
                    53);
        }
        
        // Check if the branch exists (This may be redundant)
        long saleId = TaxDTO.getSaleId();
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
                    throw new NoContentException(
                            "Sale with id " + saleId + " does not exists",
                            42);
                }
        
        // Update the  tax
        Tax TaxToUpdate = Tax.get();
        TaxToUpdate = TaxMapper.updateModel(TaxDTO, TaxToUpdate);

        // Save the  tax
        TaxToUpdate = TaxRepository.save(TaxToUpdate);

        // Return the  tax
        return TaxMapper.toDTO(TaxToUpdate);

    }

    // Create a method that deletes a  tax (from the id)
    public void delete(Long TaxId) throws NoContentException{
        // Check if the  tax exists
        Optional<Tax> Tax = TaxRepository.findById(TaxId);
        if (Tax.isEmpty()) {
                    throw new NoContentException(
                            " tax with id " + TaxId + " does not exists",
                            50);
                }
        // Delete the  tax
        TaxRepository.deleteById(TaxId);
    }
        
}
