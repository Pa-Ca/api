package com.paca.paca.reservation;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.GuestListDTO;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.service.GuestService;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.exception.exceptions.NoContentException;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private GuestService guestService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldGetAllGuest() {
        List<Guest> guests = TestUtils.castList(Guest.class, Mockito.mock(List.class));

        when(guestRepository.findAll()).thenReturn(guests);
        GuestListDTO responseDTO = guestService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingGuestInGetGuestById() {
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            guestService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Guest with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 54);
        }
    }

    @Test
    void shouldGetGuestById() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(guest));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);

        GuestDTO dtoResponse = guestService.getById(guest.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(guest.getId());
    }

    @Test
    void shouldSaveGuest() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(guestMapper.toEntity(any(GuestDTO.class))).thenReturn(guest);
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);

        GuestDTO dtoResponse = guestService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(guest.getId());
    }

    @Test
    void shouldGetNoContentDueToMissingGuestInUpdateGuest() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            guestService.update(guest.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Guest with id " + guest.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 54);
        }
    }

    @Test
    void shouldUpdateGuest() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(guest));
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(guestMapper.updateModel(any(GuestDTO.class), any(Guest.class))).thenReturn(guest);
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);

        GuestDTO dtoResponse = guestService.update(guest.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(guest.getId());
    }

    @Test
    void shouldGetNoContentDueToMissingGuestInDeleteGuest() {
        Guest guest = utils.createGuest();

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            guestService.delete(guest.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Guest with id " + guest.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 54);
        }
    }

}
