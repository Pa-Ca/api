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

        // Not changing identity document
        dto = GuestDTO.builder()
                .identityDocument(guest.getIdentityDocument() + "_test")
                .build();
        updatedGuest = guestMapper.updateModel(dto, guest);
        assertThat(updatedGuest).isNotNull();
        assertThat(updatedGuest.getIdentityDocument()).isEqualTo(guest.getIdentityDocument());
    }
}