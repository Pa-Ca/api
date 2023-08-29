package com.paca.paca.reservation;

import com.paca.paca.ServiceTest;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.dto.GuestInfoDTO;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.service.GuestService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NotFoundException;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GuestServiceTest extends ServiceTest {

    @InjectMocks
    private GuestService guestService;

    @Test
    void shouldGetNotFoundDueToMissingGuestInGetGuestById() {
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            guestService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Guest with id 1 does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 54);
        }
    }

    @Test
    void shouldGetGuestById() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);
        ClientGuest clientGuest = utils.createClientGuest(guest);

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(guest));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);
        when(clientGuestRepository.findByGuestId(any(Long.class))).thenReturn(Optional.ofNullable(clientGuest));

        GuestInfoDTO response = guestService.getById(guest.getId());
        GuestInfoDTO expected = new GuestInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingGuestInGetGuestByIdentityDocument() {
        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.empty());

        try {
            guestService.getByIdentityDocument(1L, "iden_doc_test");
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Guest with identityDocument iden_doc_test does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 40);
        }
    }

    @Test
    void shouldGetForbiddenDueToMissingReservationWithBusinessInGetGuestByIdentityDocument() {
        Guest guest = utils.createGuest();
        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.ofNullable(guest));
        when(reservationRepository.existsByBranchBusinessIdAndGuestId(any(Long.class), any(Long.class)))
                .thenReturn(false);
        when(saleRepository.existsByBranchBusinessIdAndClientGuestGuestId(any(Long.class), any(Long.class)))
                .thenReturn(false);

        try {
            guestService.getByIdentityDocument(1L, "iden_doc_test");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(),
                    "Guest with identityDocument iden_doc_test does not have a past reservation or sale with this business");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 40);
        }
    }

    @Test
    void shouldGetGuestByIdentityDocument() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);
        Reservation reservation = utils.createReservation(null, guest);
        ClientGuest clientGuest = utils.createClientGuest(guest);

        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.ofNullable(guest));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);
        when(reservationRepository.existsByBranchBusinessIdAndGuestId(any(Long.class), any(Long.class)))
                .thenReturn(true);
        when(clientGuestRepository.findByGuestId(any(Long.class))).thenReturn(Optional.ofNullable(clientGuest));

        GuestInfoDTO response = guestService.getByIdentityDocument(
                reservation.getBranch().getBusiness().getId(),
                guest.getIdentityDocument());
        GuestInfoDTO expected = new GuestInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldSaveGuest() {
        Guest guest = utils.createGuest();
        ClientGuest clientGuest = utils.createClientGuest(guest);
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.empty());
        when(guestRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(guestRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(guestMapper.toEntity(any(GuestDTO.class))).thenReturn(guest);
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);
        when(clientGuestRepository.save(any(ClientGuest.class))).thenReturn(clientGuest);

        GuestInfoDTO response = guestService.save(dto);
        GuestInfoDTO expected = new GuestInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetConflictDueToEmailAlreadyExists() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.empty());
        when(guestRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(guest));

        try {
            guestService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Guest with email " + dto.getEmail() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 61);
        }
    }

    @Test
    void shouldGetConflictDueToPhoneNumberAlreadyExists() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findByIdentityDocument(any(String.class))).thenReturn(Optional.empty());
        when(guestRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(guestRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.ofNullable(guest));

        try {
            guestService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Guest with phone number " + dto.getPhoneNumber() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 62);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingGuestInUpdateGuest() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            guestService.update(guest.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Guest with id " + guest.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 54);
        }
    }

    @Test
    void shouldUpdateGuest() {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);
        ClientGuest clientGuest = utils.createClientGuest(guest);

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(guest));
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(guestMapper.updateModel(any(GuestDTO.class), any(Guest.class))).thenReturn(guest);
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(dto);
        when(clientGuestRepository.findByGuestId(any(Long.class))).thenReturn(Optional.ofNullable(clientGuest));

        GuestInfoDTO response = guestService.update(guest.getId(), dto);
        GuestInfoDTO expected = new GuestInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingGuestInDeleteGuest() {
        Guest guest = utils.createGuest();

        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            guestService.delete(guest.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Guest with id " + guest.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 54);
        }
    }
}
