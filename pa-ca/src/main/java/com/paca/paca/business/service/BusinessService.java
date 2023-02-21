package com.paca.paca.business.service;

import com.paca.paca.business.model.Business;
import org.springframework.stereotype.Service;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.business.dto.BusinessListDTO;
import com.paca.paca.business.utils.BusinessMapper;

import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.List;
import java.util.ArrayList;

@Service
public class BusinessService {
    
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;


    public BusinessService(BusinessRepository userRepository, BusinessMapper businessMapper) {
        this.businessRepository = userRepository;
        this.businessMapper = businessMapper;
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
                        "Client with id: " + id + " does not exists",
                        28));

        BusinessDTO dto = businessMapper.toDTO(business);
        dto.setUserId(business.getUser().getId());
        return dto;
    }

}