package com.paca.paca.reservation;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.utils.ReservationMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Date;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class ReservationMapperTest {
    @InjectMocks
    private ReservationMapperImpl reservationMapper;
    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapReservationEntityToReservationDTO() {
        Reservation reservation = utils.createReservation(null, null, null);

        ReservationDTO response = reservationMapper.toDTO(reservation);
        ReservationDTO expected = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                reservation.getGuest().getId(),
                reservation.getInvoice().getId(),
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());
        assertThat(response).isEqualTo(expected);

        reservation = utils.createReservation(null, null);
        response = reservationMapper.toDTO(reservation);
        expected = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                reservation.getGuest().getId(),
                null,
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());
        assertThat(response).isEqualTo(expected);

        reservation = utils.createReservation(null);
        response = reservationMapper.toDTO(reservation);
        expected = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                null,
                null,
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapReservationDTOtoReservationEntity() {
        Branch branch = utils.createBranch(null);
        Guest guest = utils.createGuest();
        Invoice invoice = utils.createInvoice();
        ReservationDTO dto = utils.createReservationDTO(utils.createReservation(
                branch,
                guest,
                invoice));
        Reservation entity = reservationMapper.toEntity(dto, branch, guest, invoice);
        Reservation expected = new Reservation(
                dto.getId(),
                branch,
                guest,
                invoice,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());
        assertThat(entity).isEqualTo(expected);

        dto = utils.createReservationDTO(utils.createReservation(branch, guest));
        entity = reservationMapper.toEntity(dto, branch, guest, null);
        expected = new Reservation(
                dto.getId(),
                branch,
                guest,
                null,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());
        assertThat(entity).isEqualTo(expected);

        dto = utils.createReservationDTO(utils.createReservation(branch));
        entity = reservationMapper.toEntity(dto, branch, null, null);
        expected = new Reservation(
                dto.getId(),
                branch,
                null,
                null,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapReservationDTOtoReservationEntity() {
        Reservation reservation = utils.createReservation(
                utils.createBranch(null),
                utils.createGuest());

        // Not changing ID
        ReservationDTO dto = ReservationDTO.builder()
                .id(reservation.getId() + 1)
                .build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getId()).isEqualTo(reservation.getId());

        // Not changing Branch ID
        dto = ReservationDTO.builder()
                .branchId(reservation.getBranch().getId() + 1)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getBranch().getId()).isEqualTo(reservation.getBranch().getId());

        // Not changing Guest ID
        dto = ReservationDTO.builder()
                .guestId(reservation.getGuest().getId() + 1)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getGuest().getId()).isEqualTo(reservation.getGuest().getId());

        // Not changing Invoice ID
        dto = ReservationDTO.builder()
                .invoiceId(reservation.getInvoice().getId() + 1)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getInvoice().getId()).isEqualTo(reservation.getInvoice().getId());

        // Changing requestDate
        dto = ReservationDTO.builder()
                .requestDate(new Date(System.currentTimeMillis()))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getRequestDate()).isEqualTo(dto.getRequestDate());

        // Changing ReservationDateIn
        dto = ReservationDTO.builder()
                .reservationDateIn(new Date(System.currentTimeMillis()))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getReservationDateIn()).isEqualTo(dto.getReservationDateIn());

        // Changing ReservationDateOut
        dto = ReservationDTO.builder()
                .reservationDateOut(new Date(System.currentTimeMillis()))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getReservationDateIn()).isEqualTo(dto.getReservationDateOut());

        // Changing clientNumber
        dto = ReservationDTO.builder()
                .clientNumber(Short.valueOf("69"))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getClientNumber()).isEqualTo(dto.getClientNumber());

        // Changing tableNumber
        dto = ReservationDTO.builder()
                .tableNumber(Short.valueOf("69"))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getTableNumber()).isEqualTo(dto.getTableNumber());

        // Changing status
        dto = ReservationDTO.builder()
                .status(Short.valueOf("1"))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getStatus()).isEqualTo(dto.getStatus());

        // Changing price
        dto = ReservationDTO.builder()
                .price(BigDecimal.valueOf(69.69F))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getPrice()).isEqualTo(dto.getPrice());

        // Changing occasion
        dto = ReservationDTO.builder()
                .occasion("Anniversary")
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getOccasion()).isEqualTo(dto.getOccasion());

        // Changing byClient
        dto = ReservationDTO.builder()
                .byClient(Boolean.TRUE)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getByClient()).isEqualTo(dto.getByClient());
    }
}