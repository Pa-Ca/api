package com.paca.paca.branch.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.branch.utils.PaymentOptionMapper;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.branch.repository.PaymentOptionRepository;

@Service
@RequiredArgsConstructor
public class PaymentOptionService {

    private final BranchRepository branchRepository;

    private final PaymentOptionMapper paymentOptionMapper;

    private final PaymentOptionRepository paymentOptionRepository;

    public PaymentOptionDTO save(PaymentOptionDTO paymentOptionDTO) throws NotFoundException {
        long branchId = paymentOptionDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NotFoundException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        PaymentOption paymentOption = paymentOptionMapper.toEntity(paymentOptionDTO, branch.get());
        paymentOption = paymentOptionRepository.save(paymentOption);
        return paymentOptionMapper.toDTO(paymentOption);
    }

    public PaymentOptionDTO update(long id, PaymentOptionDTO paymentOptionDTO) throws NotFoundException {
        Optional<PaymentOption> paymentOption = paymentOptionRepository.findById(id);
        if (paymentOption.isEmpty()) {
            throw new NotFoundException(
                    "Payment option with id " + id + " does not exists",
                    58);
        }

        PaymentOption paymentOptionToUpdate = paymentOption.get();
        paymentOptionToUpdate = paymentOptionMapper.updateModel(paymentOptionDTO, paymentOptionToUpdate);
        paymentOptionToUpdate = paymentOptionRepository.save(paymentOptionToUpdate);

        return paymentOptionMapper.toDTO(paymentOptionToUpdate);

    }

    public void delete(Long paymentOptionId) throws NotFoundException {
        Optional<PaymentOption> paymentOption = paymentOptionRepository.findById(paymentOptionId);
        if (paymentOption.isEmpty()) {
            throw new NotFoundException(
                    "Payment option with id " + paymentOptionId + " does not exists",
                    58);
        }

        paymentOptionRepository.deleteById(paymentOptionId);
    }

}
