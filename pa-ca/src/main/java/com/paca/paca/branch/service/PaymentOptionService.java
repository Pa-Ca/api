package com.paca.paca.branch.service;

import java.util.Optional;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.repository.PaymentOptionRepository;
// import the mapper
import com.paca.paca.branch.utils.PaymentOptionMapper;
import com.paca.paca.exception.exceptions.NoContentException;
// import the DTO's
import com.paca.paca.branch.dto.PaymentOptionDTO;


// Import branch and the branch repository
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;


@Service
@RequiredArgsConstructor
public class PaymentOptionService {

    private final BranchRepository branchRepository;
    private final PaymentOptionMapper paymentOptionMapper;
    private final PaymentOptionRepository paymentOptionRepository;

    public PaymentOptionDTO save(PaymentOptionDTO paymentOptionDTO) throws NoContentException{
        // Check if the branch exists
        long branchId = paymentOptionDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
                    throw new NoContentException(
                            "Branch with id " + branchId + " does not exists",
                            20);
                }
        // Create a payment option
        PaymentOption paymentOption = paymentOptionMapper.toEntity(paymentOptionDTO, branch.get());
        // Save the payment option
        paymentOption = paymentOptionRepository.save(paymentOption);
        // Return the payment option
        return paymentOptionMapper.toDTO(paymentOption);
    }

    public PaymentOptionDTO update(long id, PaymentOptionDTO paymentOptionDTO) throws NoContentException{

        // Check if the payment option exists
        Optional<PaymentOption> paymentOption = paymentOptionRepository.findById(id);
        if (paymentOption.isEmpty()) {
                    throw new NoContentException(
                            "Payment option with id " + id + " does not exists",
                            58);
                }
        
        // Check if the branch exists (This may be redundant)
        long branchId = paymentOptionDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
                    throw new NoContentException(
                            "Branch with id " + branchId + " does not exists",
                            20);
                }
        
        // Update the payment option
        PaymentOption paymentOptionToUpdate = paymentOption.get();
        paymentOptionToUpdate = paymentOptionMapper.updateModel(paymentOptionDTO, paymentOptionToUpdate);

        // Save the payment option
        paymentOptionToUpdate = paymentOptionRepository.save(paymentOptionToUpdate);

        // Return the payment option
        return paymentOptionMapper.toDTO(paymentOptionToUpdate);

    }

    public void delete(Long paymentOptionId) throws NoContentException{
        // Check if the paymentOption exists
        Optional<PaymentOption> paymentOption = paymentOptionRepository.findById(paymentOptionId);
        if (paymentOption.isEmpty()) {
                    throw new NoContentException(
                            "Payment option with id " + paymentOptionId + " does not exists",
                            58); // Lista en docs
                }
        // Delete the payment option
        
        paymentOptionRepository.deleteById(paymentOptionId);
    }
        
}

