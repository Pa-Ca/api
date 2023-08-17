package com.paca.paca.business.service;

import com.paca.paca.user.model.User;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.business.model.Tier;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.BranchInfoDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.branch.dto.BranchInfoListDTO;
import com.paca.paca.business.utils.BusinessMapper;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BranchMapper branchMapper;

    private final TaxMapper defaultTaxMapper;

    private final BusinessMapper businessMapper;

    private final TierRepository tierRepository;

    private final UserRepository userRepository;

    private final BranchRepository branchRepository;

    private final BusinessRepository businessRepository;

    private final DefaultTaxRepository defaultTaxRepository;

    private void validateTier(String tier) throws BadRequestException {
        if (tier.isEmpty())
            throw new BadRequestException("The tier attribute not found");

        try {
            BusinessTier.valueOf(tier);
        } catch (Exception e) {
            throw new BadRequestException("The tier given is not valid");
        }
    }

    public BusinessDTO getById(Long id) throws NoContentException {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Business with id " + id + " does not exists",
                        28));

        BusinessDTO dto = businessMapper.toDTO(business);
        dto.setUserId(business.getUser().getId());
        return dto;
    }

    public BusinessDTO save(BusinessDTO dto) throws NoContentException, ConflictException {
        String email = dto.getEmail();

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isEmpty()) {
            throw new NoContentException(
                    "User with email " + dto.getEmail() + " does not exists",
                    30);
        }

        boolean businessExists = businessRepository.existsByUserEmail(email);
        if (businessExists) {
            throw new ConflictException(
                    "Business with email " + dto.getEmail() + " already exists",
                    12);
        }

        // Tier validation
        if (dto.getTier() != null)
            validateTier(dto.getTier());

        Optional<Tier> tier = tierRepository.findByName(BusinessTier.valueOf(dto.getTier()));
        if (tier.isEmpty()) {
            throw new NoContentException(
                    "Tier " + tier.get() + " does not exists",
                    38);
        }
        Business newBusiness = businessMapper.toEntity(dto, tier.get(), user.get());
        newBusiness = businessRepository.save(newBusiness);
        BusinessDTO dtoResponse = businessMapper.toDTO(newBusiness);

        return dtoResponse;
    }

    public BusinessDTO update(Long id, BusinessDTO dto) throws NoContentException {
        Optional<Business> current = businessRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Business with id " + id + " does not exists",
                    28);
        }

        Tier tier;
        if (dto.getTier() != null) {
            validateTier(dto.getTier());
            tier = tierRepository.findByName(BusinessTier.valueOf(dto.getTier())).orElseThrow(
                    () -> new NoContentException(
                            "Tier " + dto.getTier() + " does not exists",
                            38));
        } else {
            tier = current.get().getTier();
        }

        Business newBusiness = businessMapper.updateModel(dto, current.get(), tier);
        newBusiness = businessRepository.save(newBusiness);
        BusinessDTO dtoResponse = businessMapper.toDTO(newBusiness);

        return dtoResponse;
    }

    public void delete(Long id) throws NoContentException {
        Optional<Business> current = businessRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Business with id " + id + " does not exists",
                    28);
        }
        businessRepository.deleteById(id);

    }

    public BusinessDTO getByUserId(Long id) throws NoContentException {
        Business business = businessRepository.findByUserId(id)
                .orElseThrow(() -> new NoContentException(
                        "User with id " + id + " does not exists",
                        12));

        BusinessDTO dto = businessMapper.toDTO(business);
        dto.setUserId(business.getUser().getId());

        return dto;
    }

    public BranchInfoListDTO getAllBranchesById(Long id) {
        List<BranchInfoDTO> response = new ArrayList<>();
        branchRepository.findAllByBusinessId(id).forEach(branch -> {
            // Get branch info
            BranchInfoDTO dto = BranchInfoDTO.builder()
                    .branch(branchMapper.toDTO(branch))
                    .build();

            // Get default taxes info
            List<TaxDTO> defaultTaxes = new ArrayList<>();
            defaultTaxRepository.findAllByBranchId(branch.getId()).forEach(defaultTax -> {
                TaxDTO defaultTaxDTO = defaultTaxMapper.toDTO(defaultTax);
                defaultTaxes.add(defaultTaxDTO);
            });
            dto.setDefaultTaxes(defaultTaxes);

            response.add(dto);
        });

        return BranchInfoListDTO.builder().branches(response).build();
    }
}