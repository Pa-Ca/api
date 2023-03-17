package com.paca.paca.reservation;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.utils.TestUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldGetAllReservation() {
        List<Reservation> reservations = TestUtils.castList(Reservation.class, Mockito.mock(List.class));

        when(reservationRepository.findAll()).thenReturn(reservations);
        ReservationListDTO responseDTO = reservationService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingReservationInGetReservationById() {
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetReservationById() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(dto);

        ReservationDTO dtoResponse = reservationService.getById(reservation.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(reservation.getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(reservation.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSaveReservation() {
        ReservationDTO dto = utils.createReservationDTO(null);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + dto.getBranchId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldSaveReservation() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toEntity(any(ReservationDTO.class), any(Branch.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(dto);

        ReservationDTO dtoResponse = reservationService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(reservation.getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(reservation.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingReservationInUpdateReservation() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.update(reservation.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldUpdateReservation() {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.updateModel(any(ReservationDTO.class), any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(dto);

        ReservationDTO dtoResponse = reservationService.update(reservation.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(reservation.getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(reservation.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingReservationInDeleteReservation() {
        Reservation reservation = utils.createReservation(null);

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.delete(reservation.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    // cancel
    @Test
    void shouldGetNoContentDueToMissingReservationInCancelReservation() {
        ClientGroup clientGroup = utils.createClientGroup(null,null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + clientGroup.getReservation().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToCancelReservationDueToReservationAlreadyInOtherStatus() {
        ClientGroup clientGroup = utils.createClientGroup(null,null);

        //returned
        clientGroup.getReservation().setStatus(ReservationStatics.Status.returned);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + clientGroup.getReservation().getId() +
                            " can't be canceled because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        //closed
        clientGroup.getReservation().setStatus(ReservationStatics.Status.closed);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + clientGroup.getReservation().getId() +
                            " can't be canceled because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        //rejected
        clientGroup.getReservation().setStatus(ReservationStatics.Status.rejected);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + clientGroup.getReservation().getId() +
                            " can't be canceled because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }

        //canceled
        clientGroup.getReservation().setStatus(ReservationStatics.Status.canceled);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + clientGroup.getReservation().getId() +
                            " can't be canceled because it is already canceled");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 72);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInCancelReservation() {
        ClientGroup clientGroup = utils.createClientGroup(null,null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch related to reservation with id " + clientGroup.getReservation().getBranch().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 73);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInCancelReservation() {
        ClientGroup clientGroup = utils.createClientGroup(null,null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation().getBranch()));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business related to user with email " + clientGroup.getClient().getUser().getEmail()+ " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 74);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInCancelReservation() {
        ClientGroup clientGroup = utils.createClientGroup(null,null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation().getBranch()));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(clientGroup.getReservation().getBranch().getBusiness()));

        try {
            reservationService.cancel(clientGroup.getReservation().getId(), clientGroup.getClient().getUser().getEmail());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business related to user with email " + clientGroup.getClient().getUser().getEmail()+ " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 74);
        }
    }
}