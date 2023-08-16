package com.paca.paca.reservation;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.service.GuestService;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.repository.ReservationRepository;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private GuestService guestService;

    private TestUtils utils = TestUtils.builder().build();

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

        assertThat(dtoResponse).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingGuestInGetGuestByIdentityDocument() {
        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.empty());

        try {
            guestService.getByIdentityDocument(1L, "iden_doc_test");
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Guest with identityDocument iden_doc_test does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 40);
        }
    }

    @Test
    void shouldGetForbiddenDueToMissingReservationWithBusinessInGetGuestByIdentityDocument() {
        Guest guest = utils.createGuest();
        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.ofNullable(guest));
        when(reservationRepository.existsByBranchBusinessIdAndGuestId(any(Long.class), any(Long.class)))
                .thenReturn(false);

        try {
            guestService.getByIdentityDocument(1L, "iden_doc_test");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(),
                    "Guest with identityDocument iden_doc_test does not have a past reservation with this business");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 40);
        }
    }

    @Test
    void shouldGetGuestByIdentityDocument() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);
        Reservation reservation = utils.createReservation(null, guest);

        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.ofNullable(guest));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);
        when(reservationRepository.existsByBranchBusinessIdAndGuestId(any(Long.class), any(Long.class)))
                .thenReturn(true);

        GuestDTO dtoResponse = guestService.getByIdentityDocument(
                reservation.getBranch().getBusiness().getId(),
                guest.getIdentityDocument());

        assertThat(dtoResponse).isEqualTo(dto);
    }

    @Test
    void shouldSaveGuest() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(guestMapper.toEntity(any(GuestDTO.class))).thenReturn(guest);
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);

        GuestDTO dtoResponse = guestService.save(dto);

        assertThat(dtoResponse).isEqualTo(dto);
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

        assertThat(dtoResponse).isEqualTo(dto);
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
