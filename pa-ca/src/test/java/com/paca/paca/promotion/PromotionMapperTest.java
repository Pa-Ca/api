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

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(promotion.getId());
        assertThat(response.getBranchId()).isEqualTo(promotion.getBranch().getId());
        assertThat(response.getDisabled()).isEqualTo(promotion.getDisabled());
        assertThat(response.getText()).isEqualTo(promotion.getText());
    }
    
    @Test 
    void shouldMapPromotionDTOtoPromotionEntity() {
        Branch branch = utils.createBranch(null);
        PromotionDTO dto = utils.createPromotionDTO(utils.createPromotion(branch));

        Promotion promotion = promotionMapper.toEntity(dto, branch);

        assertThat(promotion).isNotNull();
        assertThat(promotion.getId()).isEqualTo(dto.getId());
        assertThat(promotion.getBranch().getId()).isEqualTo(branch.getId());
        assertThat(promotion.getDisabled()).isEqualTo(dto.getDisabled());
        assertThat(promotion.getText()).isEqualTo(dto.getText());
    }

    @Test
    void shouldPartiallyMapPromotionDTOtoPromotionEntity() {
        Promotion promotion = utils.createPromotion(null);

        // Not changing ID
        PromotionDTO dto = PromotionDTO.builder()
                .id(promotion.getId() + 1)
                .build();
        Promotion updatedPromotion = promotionMapper.updateModel(dto, promotion);
        assertThat(updatedPromotion).isNotNull();
        assertThat(updatedPromotion.getId()).isEqualTo(promotion.getId());

        // Not changing Branch ID
        dto = PromotionDTO.builder()
                .branchId(promotion.getBranch().getId() + 1)
                .build();
        updatedPromotion = promotionMapper.updateModel(dto, promotion);
        assertThat(updatedPromotion).isNotNull();
        assertThat(updatedPromotion.getBranch().getId()).isEqualTo(promotion.getBranch().getId());

        // Changing disabled
        dto = PromotionDTO.builder()
                .disabled(!promotion.getDisabled())
                .build();
        updatedPromotion = promotionMapper.updateModel(dto, promotion);
        assertThat(updatedPromotion).isNotNull();
        assertThat(updatedPromotion.getDisabled()).isEqualTo(dto.getDisabled());

        // Changing coordinates
        dto = PromotionDTO.builder()
                .text("new text_test")
                .build();
        updatedPromotion = promotionMapper.updateModel(dto, promotion);
        assertThat(updatedPromotion).isNotNull();
        assertThat(updatedPromotion.getText()).isEqualTo(dto.getText());
    }

}
