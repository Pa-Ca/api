package com.paca.paca.reservation;

import com.paca.paca.ServiceTest;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.Client;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.branch.dto.TableListDTO;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.sale.model.InsiteSaleTable;
import com.paca.paca.reservation.dto.InvoiceDTO;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationInfoDTO;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.reservation.dto.BranchReservationsInfoDTO;
import com.paca.paca.exception.exceptions.UnprocessableException;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReservationServiceTest extends ServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldGetNotFoundDueToMissingReservationInGetReservationById() {
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id 1 does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetReservationWithGuestById() {
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);
        Reservation reservation = utils.createReservation(null, null, invoice);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        GuestDTO guestDTO = utils.createGuestDTO(reservation.getGuest());

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);
        when(invoiceRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(invoice));
        when(invoiceMapper.toDTO(any(Invoice.class))).thenReturn(invoiceDTO);
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getGuest()));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(guestDTO);

        ReservationInfoDTO response = reservationService.getById(reservation.getId());
        ReservationInfoDTO expected = new ReservationInfoDTO(reservationDTO, invoiceDTO, guestDTO, null);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetReservationWithClientById() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);
        ClientGroup clientGroup = utils.createClientGroup(client, reservation);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);
        when(clientGroupRepository.findAllByReservationId(any(Long.class))).thenReturn(List.of(clientGroup));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(clientDTO);

        ReservationInfoDTO response = reservationService.getById(reservation.getId());
        ReservationInfoDTO expected = new ReservationInfoDTO(reservationDTO, null, null, clientDTO);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingBranchInSaveReservation() {
        ReservationDTO dto = utils.createReservationDTO(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.save(new ReservationInfoDTO(dto, null, null, null));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + dto.getBranchId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetBadRequestDueToClientNumberExceedsCapacityBranchInSaveReservation() {
        Branch branch = utils.createBranch(null);
        branch.setCapacity(Short.valueOf("1"));

        Reservation reservation = utils.createReservation(branch, null);
        ReservationDTO dto = utils.createReservationDTO(reservation);
        dto.setByClient(true);
        dto.setClientNumber(Short.valueOf("2"));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));

        try {
            reservationService.save(new ReservationInfoDTO(dto, null, null, null));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Requested number of client surpass branch " + dto.getBranchId() + " capacity");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetConflictDueToGuestWithEmailAlreadyExistsInSaveReservationWithGuest() {
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);
        Reservation reservation = utils.createReservation(null, null, invoice);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        GuestDTO guestDTO = utils.createGuestDTO(reservation.getGuest());

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(guestRepository.findByIdentityDocument(any(String.class)))
                .thenReturn(Optional.empty());
        when(guestRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(reservation.getGuest()));

        ReservationInfoDTO dto = new ReservationInfoDTO(reservationDTO, invoiceDTO, guestDTO, null);

        try {
            reservationService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(),
                    "Guest with email " + guestDTO.getEmail() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 61);
        }
    }

    @Test
    void shouldGetConflictDueToGuestWithPhoneNumberAlreadyExistsInSaveReservationWithGuest() {
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);
        Reservation reservation = utils.createReservation(null, null, invoice);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        GuestDTO guestDTO = utils.createGuestDTO(reservation.getGuest());

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(guestRepository.findByIdentityDocument(any(String.class)))
                .thenReturn(Optional.empty());
        when(guestRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.empty());
        when(guestRepository.findByPhoneNumber(any(String.class)))
                .thenReturn(Optional.ofNullable(reservation.getGuest()));

        ReservationInfoDTO dto = new ReservationInfoDTO(reservationDTO, invoiceDTO, guestDTO, null);

        try {
            reservationService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(),
                    "Guest with phone number " + guestDTO.getPhoneNumber() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 62);
        }
    }

    @Test
    void shouldSaveReservationWithGuest() {
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);
        Reservation reservation = utils.createReservation(null, null, invoice);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        GuestDTO guestDTO = utils.createGuestDTO(reservation.getGuest());

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(guestRepository.findByIdentityDocument(any(String.class)))
                .thenReturn(Optional.ofNullable(reservation.getGuest()));
        when(guestMapper.updateModel(any(GuestDTO.class), any(Guest.class))).thenReturn(reservation.getGuest());
        when(guestRepository.save(any(Guest.class))).thenReturn(reservation.getGuest());
        when(reservationMapper.toEntity(
                any(ReservationDTO.class),
                any(Branch.class),
                any(Guest.class)))
                .thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);
        when(invoiceRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(invoice));
        when(invoiceMapper.toDTO(any(Invoice.class))).thenReturn(invoiceDTO);
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getGuest()));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(guestDTO);

        ReservationInfoDTO expected = new ReservationInfoDTO(reservationDTO, invoiceDTO, guestDTO, null);
        ReservationInfoDTO response = reservationService.save(expected);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldSaveReservationWithClient() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);
        ClientGroup clientGroup = utils.createClientGroup(client, reservation);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(reservationMapper.toEntity(any(ReservationDTO.class), any(Branch.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);
        when(clientGroupRepository.findAllByReservationId(any(Long.class))).thenReturn(List.of(clientGroup));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(clientDTO);

        ReservationInfoDTO expected = new ReservationInfoDTO(reservationDTO, null, null, clientDTO);
        ReservationInfoDTO response = reservationService.save(expected);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingReservationInUpdateReservation() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.update(reservation.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldUpdateReservationWithGuest() {
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);
        Reservation reservation = utils.createReservation(null, null, invoice);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        GuestDTO guestDTO = utils.createGuestDTO(reservation.getGuest());

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.updateModel(any(ReservationDTO.class), any(Reservation.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);
        when(invoiceRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(invoice));
        when(invoiceMapper.toDTO(any(Invoice.class))).thenReturn(invoiceDTO);
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getGuest()));
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(guestDTO);

        ReservationInfoDTO response = reservationService.update(reservation.getId(), reservationDTO);
        ReservationInfoDTO expected = new ReservationInfoDTO(reservationDTO, invoiceDTO, guestDTO, null);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldUpdateReservationWithClient() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO reservationDTO = utils.createReservationDTO(reservation);
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);
        ClientGroup clientGroup = utils.createClientGroup(client, reservation);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.updateModel(any(ReservationDTO.class), any(Reservation.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);
        when(clientGroupRepository.findAllByReservationId(any(Long.class))).thenReturn(List.of(clientGroup));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(clientDTO);

        ReservationInfoDTO response = reservationService.update(reservation.getId(), reservationDTO);
        ReservationInfoDTO expected = new ReservationInfoDTO(reservationDTO, null, null, clientDTO);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingReservationInDeleteReservation() {
        Reservation reservation = utils.createReservation(null);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.delete(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingBranchInGetBranchReservations() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.getBranchReservations(
                    1,
                    1,
                    branch.getId(),
                    null,
                    null,
                    null,
                    null,
                    null);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branch.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetNotFoundDueToPageLessThanZeroInGetBranchReservations() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));

        try {
            reservationService.getBranchReservations(
                    -1,
                    1,
                    branch.getId(),
                    null,
                    null,
                    null,
                    null,
                    null);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page number cannot be less than zero");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 44);
        }
    }

    @Test
    void shouldGetNotFoundDueToSizeLessThanOneInGetBranchReservations() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));

        try {
            reservationService.getBranchReservations(
                    1,
                    0,
                    branch.getId(),
                    null,
                    null,
                    null,
                    null,
                    null);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page size cannot be less than one");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 45);
        }
    }

    @Test
    void shouldGetBranchReservations() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(reservationRepository.findAllByBranchIdAndFilters(
                any(Long.class),
                anyList(),
                any(Date.class),
                any(Date.class),
                isNull(),
                isNull(),
                anyString())).thenReturn(new ArrayList<>());
        when(reservationRepository.findAllByBranchIdAndFilters(
                any(Long.class),
                anyList(),
                any(Date.class),
                any(Date.class),
                anyString(),
                isNull(),
                anyString())).thenReturn(new ArrayList<>());
        when(reservationRepository.findAllByBranchIdAndFilters(
                any(Long.class),
                anyList(),
                any(Date.class),
                any(Date.class),
                isNull(),
                anyString(),
                anyString())).thenReturn(new ArrayList<>());
        when(reservationRepository.findAllByBranchIdAndFilters(
                any(Long.class),
                anyList(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull())).thenReturn(new ArrayList<>());

        BranchReservationsInfoDTO response = reservationService.getBranchReservations(
                1,
                1,
                branch.getId(),
                List.of(),
                new Date(),
                new Date(),
                "fullname test",
                "identityDocument test");
        BranchReservationsInfoDTO expected = new BranchReservationsInfoDTO(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                1,
                1,
                0);

        assertThat(response).isEqualTo(expected);
    }

    // Cancel
    @Test
    void shouldGetNotFoundDueToMissingReservationInCancelReservation() {
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(1L);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + 1L + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToCancelReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be cancelled because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be cancelled because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be cancelled because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }

        // cancelled
        reservation.setStatus(ReservationStatics.Status.CANCELED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be cancelled because it is already cancelled");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 72);
        }
    }

    // Reject
    @Test
    void shouldGetNotFoundDueToMissingReservationInRejectReservation() {
        Reservation reservation = utils.createReservation(null);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.reject(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToRejectReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.reject(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be rejected because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.reject(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be rejected because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.reject(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be rejected because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }
    }

    // Accept
    @Test
    void shouldGetNotFoundDueToMissingReservationInAcceptReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.accept(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToAcceptReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }

        // accepted
        reservation.setStatus(ReservationStatics.Status.ACCEPTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already accepted");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 76);
        }
    }

    // Start
    @Test
    void shouldGetNotFoundDueToMissingReservationInStartReservation() {
        Reservation reservation = utils.createReservation(null);
        TableListDTO tables = new TableListDTO(new ArrayList<>());

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.start(reservation.getId(), tables);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToStartReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);
        TableListDTO tables = new TableListDTO(new ArrayList<>());

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.start(reservation.getId(), tables);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't start because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.start(reservation.getId(), tables);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't start because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.start(reservation.getId(), tables);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't start because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingTableInStartReservation() {
        Reservation reservation = utils.createReservation(null, null);
        ClientGuest clientGuest = utils.createClientGuest(reservation.getGuest());
        Sale sale = utils.createSale(reservation.getBranch(), clientGuest, null);
        InsiteSale insiteSale = utils.createInsiteSale(sale, reservation);
        Table table = utils.createTable(reservation.getBranch());
        TableListDTO tables = new TableListDTO(List.of(utils.createTableDTO(table)));

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.updateModel(any(ReservationDTO.class), any(Reservation.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(clientGuestRepository.findByGuestId(any(Long.class))).thenReturn(Optional.ofNullable(clientGuest));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(taxRepository.findAllByBranchId(any(Long.class))).thenReturn(new ArrayList<>());
        when(insiteSaleRepository.save(any(InsiteSale.class))).thenReturn(insiteSale);
        when(tableRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.start(reservation.getId(), tables);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Table with id " + table.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 49);
        }
    }

    @Test
    void shouldStartReservation() {
        Reservation reservation = utils.createReservation(null, null);
        ClientGuest clientGuest = utils.createClientGuest(reservation.getGuest());
        Sale sale = utils.createSale(reservation.getBranch(), clientGuest, null);
        InsiteSale insiteSale = utils.createInsiteSale(sale, reservation);
        Table table = utils.createTable(reservation.getBranch());
        TableDTO tableDTO = utils.createTableDTO(table);
        InsiteSaleTable insiteSaleTable = utils.createInsiteSaleTable(insiteSale, table);
        SaleDTO saleDTO = utils.createSaleDTO(sale);
        GuestDTO guestDTO = utils.createGuestDTO(reservation.getGuest());
        TableListDTO tables = new TableListDTO(List.of(utils.createTableDTO(table)));

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.updateModel(any(ReservationDTO.class), any(Reservation.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(clientGuestRepository.findByGuestId(any(Long.class))).thenReturn(Optional.ofNullable(clientGuest));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(taxRepository.findAllByBranchId(any(Long.class))).thenReturn(new ArrayList<>());
        when(insiteSaleRepository.save(any(InsiteSale.class))).thenReturn(insiteSale);
        when(tableRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(table));
        when(tableMapper.toDTO(any(Table.class))).thenReturn(tableDTO);
        when(insiteSaleTableRepository.save(any(InsiteSaleTable.class))).thenReturn(insiteSaleTable);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);
        when(guestMapper.toDTO(any(Guest.class))).thenReturn(guestDTO);

        SaleInfoDTO response = reservationService.start(reservation.getId(), tables);
        SaleInfoDTO expected = new SaleInfoDTO(
                saleDTO,
                true,
                guestDTO,
                null,
                reservation.getId(),
                new ArrayList<>(),
                List.of(tableDTO),
                new ArrayList<>());

        assertThat(response).isEqualTo(expected);
    }

    // Retire
    @Test
    void shouldGetNotFoundDueToMissingReservationInRetireReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.retire(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToRetireReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.retire(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be retired because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.retire(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be retired because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.retire(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be retired because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }
    }

    // Close
    @Test
    void shouldGetNotFoundDueToMissingReservationInCloseReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.close(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToCloseReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.close(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be closed because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.close(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be closed because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.close(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be closed because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }
    }

    // Pay
    @Test
    void shouldGetNotFoundDueToMissingReservationInPayReservation() {
        Reservation reservation = utils.createReservation(null);
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.pay(reservation.getId(), invoiceDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToPayReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);

        // returned
        reservation.setStatus(ReservationStatics.Status.RETURNED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), invoiceDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        // closed
        reservation.setStatus(ReservationStatics.Status.CLOSED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), invoiceDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        // rejected
        reservation.setStatus(ReservationStatics.Status.REJECTED);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), invoiceDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }
    }

    @Test
    void shouldPay() {
        Reservation reservation = utils.createReservation(null);
        Invoice invoice = utils.createInvoice();
        InvoiceDTO invoiceDTO = utils.createInvoiceDTO(invoice);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(invoiceMapper.toEntity(any(InvoiceDTO.class))).thenReturn(invoice);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(invoiceMapper.toDTO(any(Invoice.class))).thenReturn(invoiceDTO);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        InvoiceDTO response = reservationService.pay(reservation.getId(), invoiceDTO);

        assertThat(response).isEqualTo(invoiceDTO);
    }
}