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
        BusinessDTO expected = new BusinessDTO(
                business.getId(),
                business.getUser().getId(),
                business.getUser().getEmail(),
                business.getTier().getName().name(),
                business.getName(),
                business.getVerified(),
                business.getPhoneNumber());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapBusinessDTOtoBusinessEntity() {
        User user = utils.createUser();
        Tier tier = utils.createTier(BusinessTier.basic);
        BusinessDTO dto = utils.createBusinessDTO(utils.createBusiness(user, tier));

        Business entity = businessMapper.toEntity(dto, tier, user);
        Business expected = new Business(
                dto.getId(),
                user,
                tier,
                dto.getName(),
                dto.getVerified(),
                dto.getPhoneNumber());

        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapBusinessDTOtoBusinessEntity() {
        Business business = utils.createBusiness(null);
        Tier tier = utils.createTier(BusinessTier.basic);

        BusinessDTO dto = new BusinessDTO(
                business.getId() + 1,
                business.getUser().getId() + 1,
                business.getUser().getEmail() + ".",
                BusinessTier.premium.name(),
                business.getName() + ".",
                !business.getVerified(),
                business.getPhoneNumber() + ".");
        Business response = businessMapper.updateModel(dto, business, tier);
        Business expected = new Business(
                business.getId(),
                business.getUser(),
                tier,
                dto.getName(),
                dto.getVerified(),
                dto.getPhoneNumber());

        assertThat(response).isEqualTo(expected);
    }
}
