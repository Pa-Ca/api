package com.paca.paca.sale.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxMapper TaxMapper;

    private final TaxRepository TaxRepository;

    public TaxDTO update(long id, TaxDTO TaxDTO) throws NoContentException, BadRequestException {
        Optional<Tax> Tax = TaxRepository.findById(id);
        if (Tax.isEmpty()) {
            throw new NoContentException(
                    "Tax with id " + id + " does not exists",
                    52);
        }

        Short TaxType = TaxDTO.getType();
        if (TaxType != null && !TaxStatics.Types.isTypeValid(TaxType)) {
            throw new BadRequestException(
                    "Invalid tax type:" + TaxType,
                    53);
        }

        Tax TaxToUpdate = Tax.get();
        TaxToUpdate = TaxMapper.updateModel(TaxDTO, TaxToUpdate);
        TaxToUpdate = TaxRepository.save(TaxToUpdate);

        return TaxMapper.toDTO(TaxToUpdate);

    }

    public void delete(Long TaxId) throws NoContentException {
        Optional<Tax> Tax = TaxRepository.findById(TaxId);
        if (Tax.isEmpty()) {
            throw new NoContentException(
                    "Tax with id " + TaxId + " does not exists",
                    52);
        }

        TaxRepository.deleteById(TaxId);
    }

}
