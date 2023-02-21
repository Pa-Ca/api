package com.paca.paca.business;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.dto.BusinessDTO;

import org.junit.jupiter.api.extension.ExtendWith;
import com.paca.paca.business.utils.BusinessMapperImpl;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class BusinessMapperTest {

    @InjectMocks
    private BusinessMapperImpl businessMapper;

    private TestUtils utils = TestUtils.builder().build();
    
    @Test
    void shouldMapBusinessEntityToBusinessDTO() throws ParseException {
        Business business = utils.createBusiness(null);

        BusinessDTO response = businessMapper.toDTO(business);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(business.getId());
        assertThat(response.getUserId()).isEqualTo(business.getUser().getId());
        assertThat(response.getTier()).isEqualTo(business.getTier().getName().name());
        assertThat(response.getName()).isEqualTo(business.getName());
        assertThat(response.getVerified()).isEqualTo(business.getVerified());
    }
}
