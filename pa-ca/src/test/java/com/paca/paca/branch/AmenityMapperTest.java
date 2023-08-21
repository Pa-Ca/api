package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.branch.utils.AmenityMapperImpl;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class AmenityMapperTest {

    @InjectMocks
    private AmenityMapperImpl amenityMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapAmenityEntityToAmenityDTO() {
        Amenity amenity = utils.createAmenity();

        AmenityDTO response = amenityMapper.toDTO(amenity);
        AmenityDTO expected = new AmenityDTO(amenity.getId(), amenity.getName());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapAmenityDTOtoAmenityEntity() {
        AmenityDTO amenityDTO = utils.createAmenityDTO(null);

        Amenity response = amenityMapper.toEntity(amenityDTO);
        Amenity expected = new Amenity(amenityDTO.getId(), amenityDTO.getName());

        assertThat(response).isEqualTo(expected);
    }
}
