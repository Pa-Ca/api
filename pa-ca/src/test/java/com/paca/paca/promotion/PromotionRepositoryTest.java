package com.paca.paca.promotion;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.model.Promotion;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PromotionRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllPromotionsByBranchId() {
        int nPromotions = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nPromotions; i++) {
            utils.createPromotion(branch);
            utils.createPromotion(null);
        }

        List<Promotion> promotions = promotionRepository.findAllByBranchId(branch.getId());

        assertThat(promotions.size()).isEqualTo(nPromotions);
    }

    @Test
    void shouldCheckThatPromotionExistsByIdAndBranch_Business_Id() {
        Promotion promotion = utils.createPromotion(null);

        boolean expected = promotionRepository.existsByIdAndBranch_Business_Id(promotion.getId(),
                promotion.getBranch().getBusiness().getId());

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatPromotionDoesNotExistsByIdAndBranch_Business_Id() {
        Promotion promotion = utils.createPromotion(null);

        boolean expected = promotionRepository.existsByIdAndBranch_Business_Id(promotion.getId(),
                promotion.getBranch().getBusiness().getId() + 1);

        assertThat(expected).isFalse();
    }
}
