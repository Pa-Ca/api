package com.paca.paca.reservation;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.utils.GuestMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class GuestMapperTest {
    @InjectMocks
    private GuestMapperImpl guestMapper;
    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapGuestEntityToGuestDTO() {
        Guest guest = utils.createGuest();

        GuestDTO response = guestMapper.toDTO(guest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(guest.getId());
        assertThat(response.getName()).isEqualTo(guest.getName());
        assertThat(response.getSurname()).isEqualTo(guest.getSurname());
        assertThat(response.getEmail()).isEqualTo(guest.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(guest.getPhoneNumber());
    }

    @Test
    void shouldMapGuestDTOtoGuestEntity() {
        GuestDTO dto = utils.createGuestDTO(utils.createGuest());
        Guest entity = guestMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getSurname()).isEqualTo(dto.getSurname());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
        assertThat(entity.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
    }

    @Test
    void shouldPartiallyMapGuestDTOtoGuestEntity() {
        Guest guest = utils.createGuest();

        // Not changing ID
        GuestDTO dto = GuestDTO.builder()
                .id(guest.getId() + 1)
                .build();
        Guest updatedGuest = guestMapper.updateModel(dto, guest);
        assertThat(updatedGuest).isNotNull();
        assertThat(updatedGuest.getId()).isEqualTo(guest.getId());

        // Changing name
        dto = GuestDTO.builder()
                .name(guest.getName() + "_test")
                .build();
        updatedGuest = guestMapper.updateModel(dto, guest);
        assertThat(updatedGuest).isNotNull();
        assertThat(updatedGuest.getName()).isEqualTo(dto.getName());

        // Changing surname
        dto = GuestDTO.builder()
                .surname(guest.getSurname() + "_test")
                .build();
        updatedGuest = guestMapper.updateModel(dto, guest);
        assertThat(updatedGuest).isNotNull();
        assertThat(updatedGuest.getSurname()).isEqualTo(dto.getSurname());

        // Changing email
        dto = GuestDTO.builder()
                .email(guest.getEmail() + "_test")
                .build();
        updatedGuest = guestMapper.updateModel(dto, guest);
        assertThat(updatedGuest).isNotNull();
        assertThat(updatedGuest.getEmail()).isEqualTo(dto.getEmail());

        // Changing phone number
        dto = GuestDTO.builder()
                .phoneNumber(guest.getPhoneNumber() + "_test")
                .build();
        updatedGuest = guestMapper.updateModel(dto, guest);
        assertThat(updatedGuest).isNotNull();
        assertThat(updatedGuest.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
    }
}