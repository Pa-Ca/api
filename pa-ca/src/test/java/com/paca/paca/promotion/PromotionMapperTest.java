package com.paca.paca.promotion;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.promotion.utils.PromotionMapperImpl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class PromotionMapperTest {

    @InjectMocks
    private PromotionMapperImpl promotionMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapPromotionEntityToPromotionDTO() {
        Promotion promotion = utils.createPromotion(null);

        PromotionDTO response = promotionMapper.toDTO(promotion);
        PromotionDTO expected = new PromotionDTO(
                promotion.getId(),
                promotion.getBranch().getId(),
                promotion.getText(),
                promotion.getDisabled());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapPromotionDTOtoPromotionEntity() {
        Branch branch = utils.createBranch(null);
        PromotionDTO dto = utils.createPromotionDTO(utils.createPromotion(branch));

        Promotion promotion = promotionMapper.toEntity(dto, branch);
        Promotion expected = new Promotion(
                dto.getId(),
                branch,
                dto.getText(),
                dto.getDisabled());

        assertThat(promotion).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapPromotionDTOtoPromotionEntity() {
        Branch branch = utils.createBranch(null);
        Promotion promotion = utils.createPromotion(branch);

        PromotionDTO dto = new PromotionDTO(
                promotion.getId() + 1,
                branch.getId() + 1,
                promotion.getText() + ".",
                !promotion.getDisabled());
        Promotion response = promotionMapper.updateModel(dto, promotion);
        Promotion expected = new Promotion(
                promotion.getId(),
                branch,
                dto.getText(),
                dto.getDisabled());

        assertThat(response).isEqualTo(expected);
    }

}
