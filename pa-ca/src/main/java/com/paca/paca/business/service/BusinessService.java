package com.paca.paca.business.service;

import com.paca.paca.user.model.User;
import com.paca.paca.business.model.Tier;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.dto.BranchInfoDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.branch.dto.BranchInfoListDTO;
import com.paca.paca.business.dto.BusinessListDTO;
import com.paca.paca.branch.utils.DefaultTaxMapper;
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
import com.paca.paca.user.statics.UserStatics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final DefaultTaxRepository defaultTaxRepository;

    private final BusinessRepository businessRepository;

    private final BranchRepository branchRepository;

    private final TierRepository tierRepository;

    private final BranchMapper branchMapper;

    private final BusinessMapper businessMapper;

    private final UserRepository userRepository;

    private final DefaultTaxMapper defaultTaxMapper;

    private void validateTier(String tier) throws BadRequestException {
        if (tier.isEmpty())
            throw new BadRequestException("The tier attribute not found");

        try {
            BusinessTier.valueOf(tier);
        } catch (Exception e) {
            throw new BadRequestException("The tier given is not valid");
        }
    }

    public BusinessListDTO getAll() {
        List<BusinessDTO> response = new ArrayList<>();
        businessRepository.findAll().forEach(business -> {
            BusinessDTO dto = businessMapper.toDTO(business);
            dto.setUserId(business.getUser().getId());
            response.add(dto);
        });

        return BusinessListDTO.builder().business(response).build();
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

        // Change user status to fully registered
        user.get().setRegistrationStatus(UserStatics.RegistrationStatus.registered);
        userRepository.save(user.get());

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
            BranchInfoDTO dto = new BranchInfoDTO(branchMapper.toDTO(branch));

            // Get default taxes info
            List<DefaultTaxDTO> defaultTaxes = new ArrayList<>();
            defaultTaxRepository.findAllByBranchId(branch.getId()).forEach(defaultTax -> {
                DefaultTaxDTO defaultTaxDTO = defaultTaxMapper.toDTO(defaultTax);
                defaultTaxes.add(defaultTaxDTO);
            });
            dto.setDefaultTaxes(defaultTaxes);

            response.add(dto);
        });

        return BranchInfoListDTO.builder().branches(response).build();
    }
}