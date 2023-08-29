package com.paca.paca.promotion.service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.promotion.utils.PromotionMapper;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.promotion.repository.PromotionRepository;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionMapper promotionMapper;

    private final PromotionRepository promotionRepository;

    private final BranchRepository branchRepository;

    public PromotionDTO getById(Long id) throws NotFoundException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Promotion with id " + id + " does not exists",
                        26));

        PromotionDTO dto = promotionMapper.toDTO(promotion);
        return dto;
    }

    public PromotionDTO save(PromotionDTO dto) throws NotFoundException {
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NotFoundException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }

        Promotion newPromotion = promotionMapper.toEntity(dto, branch.get());
        newPromotion = promotionRepository.save(newPromotion);

        PromotionDTO dtoResponse = promotionMapper.toDTO(newPromotion);

        return dtoResponse;
    }

    public PromotionDTO update(Long id, PromotionDTO dto) throws NotFoundException {
        Optional<Promotion> current = promotionRepository.findById(id);
        if (current.isEmpty()) {
            throw new NotFoundException(
                    "Promotion with id " + id + " does not exists",
                    26);
        }

        Promotion newPromotion = promotionMapper.updateModel(dto, current.get());
        newPromotion = promotionRepository.save(newPromotion);
        PromotionDTO dtoResponse = promotionMapper.toDTO(newPromotion);

        return dtoResponse;
    }

    public void delete(Long id) throws NotFoundException {
        Optional<Promotion> current = promotionRepository.findById(id);
        if (current.isEmpty()) {
            throw new NotFoundException(
                    "Promotion with id " + id + " does not exists",
                    26);
        }
        promotionRepository.deleteById(id);
    }
}
