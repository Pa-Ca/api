package com.paca.paca.reservation;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.repository.ClientGroupRepository;
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
    private ClientGroupRepository clientGroupRepository;
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
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(1L, "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + 1L + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToCancelReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        //returned
        reservation.setStatus(ReservationStatics.Status.returned);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be canceled because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        //closed
        reservation.setStatus(ReservationStatics.Status.closed);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be canceled because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        //rejected
        reservation.setStatus(ReservationStatics.Status.rejected);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be canceled because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }

        //canceled
        reservation.setStatus(ReservationStatics.Status.canceled);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.cancel(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be canceled because it is already canceled");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 72);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInCancelReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch related to reservation with id " + reservation.getBranch().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 73);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInCancelReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            reservationService.cancel(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business related to user with email " + "loquesea@email.com"+ " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 74);
        }
    }

    // accept
    @Test
    void shouldGetNoContentDueToMissingReservationInAcceptReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToAcceptReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        //returned
        reservation.setStatus(ReservationStatics.Status.returned);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        //closed
        reservation.setStatus(ReservationStatics.Status.closed);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        //rejected
        reservation.setStatus(ReservationStatics.Status.rejected);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }

        //accepted
        reservation.setStatus(ReservationStatics.Status.accepted);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be accepted because it is already accepted");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 76);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInAcceptReservation() {
        Reservation reservation = utils.createReservation(null);
        reservation.setStatus(ReservationStatics.Status.paid);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch related to reservation with id " + reservation.getBranch().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 73);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInAcceptReservation() {
        Reservation reservation = utils.createReservation(null);
        reservation.setStatus(ReservationStatics.Status.paid);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            reservationService.accept(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business related to user with email " + "loquesea@email.com"+ " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 74);
        }
    }

    // reject
    @Test
    void shouldGetNoContentDueToMissingReservationInRejectReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.reject(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToRejectReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);

        //returned
        reservation.setStatus(ReservationStatics.Status.returned);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.reject(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be rejected because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        //closed
        reservation.setStatus(ReservationStatics.Status.closed);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.reject(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be rejected because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        //rejected
        reservation.setStatus(ReservationStatics.Status.rejected);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.reject(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be rejected because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInRejectReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.reject(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch related to reservation with id " + reservation.getBranch().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 73);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInRejectReservation() {
        Reservation reservation = utils.createReservation(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation.getBranch()));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            reservationService.reject(reservation.getId(), "loquesea@email.com");
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business related to user with email " + "loquesea@email.com"+ " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 74);
        }
    }

    // pay
    @Test
    void shouldGetNoContentDueToMissingReservationInPayReservation() {
        Reservation reservation = utils.createReservation(null);
        ReservationPaymentDTO reservationPaymentDTO = utils.createReservationPaymentDTO(null);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reservationService.pay(reservation.getId(), "loquesea@email.com", reservationPaymentDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Reservation with id " + reservation.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetBadRequestWhenTryToPayReservationDueToReservationAlreadyInOtherStatus() {
        Reservation reservation = utils.createReservation(null);
        ReservationPaymentDTO reservationPaymentDTO = utils.createReservationPaymentDTO(null);

        //returned
        reservation.setStatus(ReservationStatics.Status.returned);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), "loquesea@email.com", reservationPaymentDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already returned");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 69);
        }

        //closed
        reservation.setStatus(ReservationStatics.Status.closed);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), "loquesea@email.com", reservationPaymentDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already closed");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 70);
        }

        //rejected
        reservation.setStatus(ReservationStatics.Status.rejected);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), "loquesea@email.com", reservationPaymentDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already rejected");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 71);
        }

        //paid
        reservation.setStatus(ReservationStatics.Status.paid);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));

        try {
            reservationService.pay(reservation.getId(), "loquesea@email.com", reservationPaymentDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(),
                    "Reservation with id " + reservation.getId() +
                            " can't be paid because it is already paid");
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 77);
        }
    }

//    @Test
//    void shouldGetNoContentDueToMissingBranchInPayReservation() {
//        Reservation reservation = utils.createReservation(null);
//        ReservationPaymentDTO reservationPaymentDTO = utils.createReservationPaymentDTO(null);
//        ClientGroup clientGroup = utils.createClientGroup(null,reservation);
//        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(reservation));
//        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//        when(clientGroupRepository.findByClientIdAndReservationId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
//
//        try {
//            reservationService.pay(reservation.getId(), "loquesea@email.com", reservationPaymentDTO);
//            TestCase.fail();
//        } catch (Exception e) {
//            Assert.assertTrue(e instanceof NoContentException);
//            Assert.assertEquals(e.getMessage(), "Client Group related to client with id " + clientGroup.getClient().getId() +
//                    " and reservation with id "+ reservation.getId() + " does not exists");
//            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 79);
//        }
//    }
}