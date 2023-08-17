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
        GuestDTO expected = new GuestDTO(
                guest.getId(),
                guest.getName(),
                guest.getSurname(),
                guest.getEmail(),
                guest.getPhoneNumber(),
                guest.getIdentityDocument());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapGuestDTOtoGuestEntity() {
        GuestDTO dto = utils.createGuestDTO(utils.createGuest());

        Guest entity = guestMapper.toEntity(dto);
        Guest expected = new Guest(
                dto.getId(),
                dto.getName(),
                dto.getSurname(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getIdentityDocument());

        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapGuestDTOtoGuestEntity() {
        Guest guest = utils.createGuest();

        GuestDTO dto = new GuestDTO(
                guest.getId() + 1,
                guest.getName() + ".",
                guest.getSurname() + ".",
                guest.getEmail() + ".",
                guest.getPhoneNumber() + ".",
                guest.getIdentityDocument() + ".");
        Guest response = guestMapper.updateModel(dto, guest);
        Guest expected = new Guest(
                guest.getId(),
                dto.getName(),
                dto.getSurname(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getIdentityDocument());

        assertThat(response).isEqualTo(expected);
    }
}