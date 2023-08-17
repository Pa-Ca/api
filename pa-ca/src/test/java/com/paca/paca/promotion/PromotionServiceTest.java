package com.paca.paca.promotion;

import org.junit.Assert;
import org.mockito.Mock;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import junit.framework.TestCase;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.promotion.utils.PromotionMapper;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.promotion.service.PromotionService;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.promotion.repository.PromotionRepository;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionService promotionService;

    private TestUtils utils = TestUtils.builder().build();

    @Test 
    void shouldGetNoContentDueToMissingPromotionInGetPromotionById() {
        when(promotionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            promotionService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Promotion with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 26);
        }
    }

    @Test
    void shouldGetPromotionById() {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(promotion));
        when(promotionMapper.toDTO(any(Promotion.class))).thenReturn(dto);

        PromotionDTO response = promotionService.getById(promotion.getId());

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSavePromotion() {
        PromotionDTO dto = utils.createPromotionDTO(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            promotionService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + dto.getBranchId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldSavePromotion() {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(promotion.getBranch()));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(promotionMapper.toEntity(any(PromotionDTO.class), any(Branch.class))).thenReturn(promotion);
        when(promotionMapper.toDTO(any(Promotion.class))).thenReturn(dto);

        PromotionDTO response = promotionService.save(dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingPromotionInUpdatePromotion() {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            promotionService.update(promotion.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Promotion with id " + promotion.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 26);
        }
    }

    @Test
    void shouldUpdatePromotion() {
        Promotion promotion = utils.createPromotion(null);
        PromotionDTO dto = utils.createPromotionDTO(promotion);

        when(promotionRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(promotion));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(promotionMapper.updateModel(any(PromotionDTO.class), any(Promotion.class))).thenReturn(promotion);
        when(promotionMapper.toDTO(any(Promotion.class))).thenReturn(dto);

        PromotionDTO response = promotionService.update(promotion.getId(), dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingPromotionInDeletePromotion() {
        Promotion promotion = utils.createPromotion(null);

        when(promotionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            promotionService.delete(promotion.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Promotion with id " + promotion.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 26);
        }
    }
}
