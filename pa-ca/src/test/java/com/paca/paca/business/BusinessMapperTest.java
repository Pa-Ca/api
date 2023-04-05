package com.paca.paca.business;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.user.model.User;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.business.model.Tier;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.business.utils.BusinessMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class BusinessMapperTest {

    @InjectMocks
    private BusinessMapperImpl businessMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapBusinessEntityToBusinessDTO() {
        Business business = utils.createBusiness(null);

        BusinessDTO response = businessMapper.toDTO(business);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(business.getId());
        assertThat(response.getUserId()).isEqualTo(business.getUser().getId());
        assertThat(response.getTier()).isEqualTo(business.getTier().getName().name());
        assertThat(response.getName()).isEqualTo(business.getName());
        assertThat(response.getVerified()).isEqualTo(business.getVerified());
    }

    @Test
    void shouldMapBusinessDTOtoBusinessEntity() {
        User user = utils.createUser();
        Tier tier = utils.createTier(BusinessTier.basic);
        BusinessDTO dto = utils.createBusinessDTO(utils.createBusiness(user, tier));

        Business entity = businessMapper.toEntity(dto, tier, user);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getUser().getId()).isEqualTo(user.getId());
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getVerified()).isEqualTo(dto.getVerified());
        assertThat(entity.getTier().getName().name()).isEqualTo(dto.getTier());
    }

    @Test
    void shouldPartiallyMapBusinessDTOtoBusinessEntity() {
        Business business = utils.createBusiness(null);
        Tier tier = utils.createTier(BusinessTier.basic);

        // Not changing ID
        BusinessDTO dto = BusinessDTO.builder()
                .id(business.getId() + 1)
                .build();
        Business updatedBusiness = businessMapper.updateModel(dto, business, tier);
        assertThat(updatedBusiness).isNotNull();
        assertThat(updatedBusiness.getId()).isEqualTo(business.getId());

        // Not changing User ID
        dto = BusinessDTO.builder()
                .userId(business.getUser().getId() + 1)
                .build();
        updatedBusiness = businessMapper.updateModel(dto, business, tier);
        assertThat(updatedBusiness).isNotNull();
        assertThat(updatedBusiness.getUser().getId()).isEqualTo(business.getUser().getId());

        // Changing name
        dto = BusinessDTO.builder()
                .name("m_test")
                .build();
        updatedBusiness = businessMapper.updateModel(dto, business, tier);
        assertThat(updatedBusiness).isNotNull();
        assertThat(updatedBusiness.getName()).isEqualTo(dto.getName());

        // Changing verified
        dto = BusinessDTO.builder()
                .verified(true)
                .build();
        updatedBusiness = businessMapper.updateModel(dto, business, tier);
        assertThat(updatedBusiness).isNotNull();
        assertThat(updatedBusiness.getVerified()).isEqualTo(dto.getVerified());

        // Changing tier
        dto = BusinessDTO.builder()
                .tier(BusinessTier.premium.name())
                .build();
        Tier newTier = utils.createTier(BusinessTier.premium);

        updatedBusiness = businessMapper.updateModel(dto, business, newTier);
        assertThat(updatedBusiness).isNotNull();
        assertThat(updatedBusiness.getTier().getName().name()).isEqualTo(dto.getTier());
    }
}
