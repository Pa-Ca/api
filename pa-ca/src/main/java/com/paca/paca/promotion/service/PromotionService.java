package com.paca.paca.promotion.service;

import com.paca.paca.promotion.utils.PromotionMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.promotion.repository.PromotionRepository;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionMapper promotionMapper;

    private final PromotionRepository promotionRepository;

    private final BranchRepository branchRepository;

    public ResponseEntity<PromotionListDTO> getAll() {
        List<PromotionDTO> response = new ArrayList<>();
        promotionRepository.findAll().forEach(promotion -> {
            PromotionDTO dto = promotionMapper.toDTO(promotion);
            response.add(dto);
        });

        return ResponseEntity.ok(PromotionListDTO.builder().promotions(response).build());
    }

    public ResponseEntity<PromotionDTO> getById(Long id) throws NoContentException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Promotion with id " + id + " does not exists",
                        26));

        PromotionDTO dto = promotionMapper.toDTO(promotion);
        return new ResponseEntity<PromotionDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<PromotionDTO> save(PromotionDTO dto) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }

        Promotion newPromotion = promotionMapper.toEntity(dto, branch.get());
        newPromotion = promotionRepository.save(newPromotion);

        PromotionDTO dtoResponse = promotionMapper.toDTO(newPromotion);

        return new ResponseEntity<PromotionDTO>(dtoResponse, HttpStatus.OK);
    }

    public ResponseEntity<PromotionDTO> update(Long id, PromotionDTO dto) throws NoContentException {
        Optional<Promotion> current = promotionRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Promotion with id: " + id + " does not exists",
                    26);
        }

        Promotion newPromotion = promotionMapper.updateModel(dto, current.get());
        newPromotion = promotionRepository.save(newPromotion);
        PromotionDTO dtoResponse = promotionMapper.toDTO(newPromotion);

        return new ResponseEntity<PromotionDTO>(dtoResponse, HttpStatus.OK);
    }

    public void delete(Long id) throws NoContentException {
        Optional<Promotion> current = promotionRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Promotion with id: " + id + " does not exists",
                    26);
        }
        promotionRepository.deleteById(id);
    }
}
